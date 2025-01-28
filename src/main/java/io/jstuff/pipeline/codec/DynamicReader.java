/*
 * @(#) DynamicReader.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2023 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.jstuff.pipeline.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import io.jstuff.pipeline.AbstractIntAcceptor;
import io.jstuff.pipeline.IntAcceptor;

public class DynamicReader extends Reader {

    private final InputStream inputStream;
    private final CharAcceptor charAcceptor;
    private final DynamicDecoder<Void> pipeline;
    private final char[] overflow;
    private int overflowIndex;

    /**
     * Construct a {@code DynamicReader} for the supplied {@link InputStream}.
     *
     * @param   inputStream     the {@link InputStream}
     */
    public DynamicReader(InputStream inputStream) {
        this(inputStream, null);
    }

    /**
     * Construct a {@code DynamicReader} for the supplied {@link InputStream}, switching immediately to the specified
     * {@link Charset} if specified.  This bypasses much of the functionality of the {@link DynamicDecoder}, but
     * allowing the {@link Charset} to be specified in this way simplifies the creation of a {@code DynamicReader} in
     * those cases where the encoding may be, but is not always, known in advance.
     *
     * @param   inputStream     the {@link InputStream}
     * @param   charset         the {@link Charset} if known, or {@code null} to allow dynamic encoding determination
     */
    public DynamicReader(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        charAcceptor = new CharAcceptor();
        pipeline = new DynamicDecoder<>(new CodePoint_UTF16<>(charAcceptor), charset);
        overflow = new char[8];
        overflowIndex = 0;
    }

    /**
     * Reads characters into a portion of an array.  This method will block until some input is available, an I/O error
     * occurs, or the end of the stream is reached.
     *
     * @param   cbuf    Destination buffer
     * @param   off     Offset at which to start storing characters
     * @param   len     Maximum number of characters to read
     * @return          The number of characters read, or -1 if the end of the stream has been reached
     * @throws  IOException If an I/O error occurs
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (overflowIndex > 0) {
            if (overflowIndex > len) {
                overflowIndex -= len;
                System.arraycopy(overflow, 0, cbuf, off, len);
                System.arraycopy(overflow, len, overflow, 0, overflowIndex);
                return len;
            }
            System.arraycopy(overflow, 0, cbuf, off, overflowIndex);
            if (overflowIndex == len) {
                overflowIndex = 0;
                return len;
            }
            off += overflowIndex;
            len -= overflowIndex;
            overflowIndex = 0;
        }
        charAcceptor.setBuffer(cbuf, off, len);
        while (!charAcceptor.isFinished()) {
            try {
                pipeline.accept(inputStream.read());
            }
            catch (Exception e) {
                throw new IOException("Unexpected exception in pipeline", e);
            }
        }
        int count = charAcceptor.getCount();
        if (count == 0 && charAcceptor.isClosed())
            return -1;
        return count;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    /**
     * Switch to the specified delegate.
     *
     * @param   delegate    the new delegate
     */
    public void switchTo(IntAcceptor<? extends Void> delegate) {
        pipeline.switchTo(delegate);
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charset     the {@link Charset}
     */
    public void switchTo(Charset charset) {
        switchTo(DecoderFactory.getDecoder(charset, pipeline.getDownstream()));
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charsetName the character set name
     */
    public void switchTo(String charsetName) {
        switchTo(DecoderFactory.getDecoder(charsetName, pipeline.getDownstream()));
    }

    private class CharAcceptor extends AbstractIntAcceptor<Void> {

        private char[] cbuf;
        private int off;
        private int len;
        private int count;

        public void setBuffer(char[] cbuf, int off, int len) {
            this.cbuf = cbuf;
            this.off = off;
            this.len = len;
            count = 0;
        }

        /**
         * Accept an {@code int}, after {@code closed} check and test for end of data.  Store the c
         *
         * @param   value       the input value
         */
        @Override
        public void acceptInt(int value) {
            if (count < len)
                cbuf[off + count++] = (char)value;
            else
                overflow[overflowIndex++] = (char)value;
        }

        public boolean isFinished() {
            return isClosed() || count >= len;
        }

        public int getCount() {
            return count;
        }

    }

}
