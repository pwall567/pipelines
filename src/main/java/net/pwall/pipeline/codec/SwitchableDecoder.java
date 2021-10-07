/*
 * @(#) UTF8_CodePoint.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020 Peter Wall
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

package net.pwall.pipeline.codec;

import java.nio.charset.Charset;

import net.pwall.pipeline.AbstractIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * A switchable decoder that allows the encoding of an input stream to be determined from the data in the early part of
 * the stream.
 *
 * <p>An XML file declares its encoding on the prefix line, and an HTML file can contain a META tag specifying the
 * content type and encoding of the data.  These rely on the declaration occurring early in the file, before any
 * characters outside the base ASCII set are expected to be encountered.</p>
 *
 * <p>This decoder allows the reading of an input stream to commence in ASCII decoding, switching to the full decoding
 * once the declaration has been read.</p>
 *
 * @author  Peter Wall
 * @param   <R>     the "Result" class (may be {@link Void})
 */
public class SwitchableDecoder<R> extends AbstractIntPipeline<R> {

    protected IntAcceptor<? extends R> delegate;

    /**
     * Construct a {@code SwitchableDecoder} with the given downstream {@link IntAcceptor}.
     *
     * @param downstream    the {@link IntAcceptor}
     */
    public SwitchableDecoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
        delegate = null;
    }

    /**
     * Accept a character, after {@code closed} check and test for end of data.  Forward the character to the delegate
     * if present, otherwise emit it directly (with no conversion).
     *
     * @param   value       the input value
     * @throws  Exception   if thrown by a {@code close()} method
     */
    @Override
    public void acceptInt(int value) throws Exception {
        if (delegate != null)
            delegate.accept(value);
        else
            emit(value);
    }

    /**
     * Return {@code true} if all sequences in the input to this stage of the pipeline are complete, that is, the input
     * is not in the middle of a sequence requiring more data.
     *
     * @return {@code true} if the input is in the "complete" state
     */
    @Override
    public boolean isStageComplete() {
        return delegate == null || delegate.isComplete();
    }

    /**
     * Switch to the specified delegate.
     *
     * @param   delegate    the new delegate
     * @throws  Exception   if thrown by downstream
     */
    public void switchTo(IntAcceptor<? extends R> delegate) throws Exception {
        this.delegate = delegate;
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charset     the {@link Charset}
     * @throws  Exception   if thrown by downstream
     */
    public void switchTo(Charset charset) throws Exception {
        switchTo(DecoderFactory.getDecoder(charset, getDownstream()));
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charsetName the character set name
     * @throws  Exception   if thrown by downstream
     */
    public void switchTo(String charsetName) throws Exception {
        switchTo(DecoderFactory.getDecoder(charsetName, getDownstream()));
    }

}
