/*
 * @(#) IntAcceptor.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2023, 2025 Peter Wall
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

package io.jstuff.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

/**
 * An acceptor that takes an integer value.  Includes default functions to cater for the common cases of strings or byte
 * arrays being used for integer values.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public interface IntAcceptor<R> extends BaseAcceptor<R>, IntConsumer {

    /**
     * Accept a value.
     *
     * @param   value       the value to be processed
     */
    void accept(int value);

    /**
     * Accept a set of values from an {@link Iterator}.
     *
     * @param   iterator    the {@link Iterator}
     */
    default void accept(Iterator<Integer> iterator) {
        while (iterator.hasNext())
            accept(iterator.next());
    }

    /**
     * Accept a set of values from an {@link Enumeration}.
     *
     * @param   enumeration the {@link Enumeration}
     */
    default void accept(Enumeration<Integer> enumeration) {
        while (enumeration.hasMoreElements())
            accept(enumeration.nextElement());
    }

    /**
     * Accept a set of values from an {@link Iterable}.
     *
     * @param   iterable    the {@link Iterable}
     */
    default void accept(Iterable<Integer> iterable) {
        accept(iterable.iterator());
    }

    /**
     * Accept a set of values from a {@link Stream}.
     *
     * @param   stream      the {@link Stream}
     */
    default void accept(Stream<Integer> stream) {
        accept(stream.iterator());
    }

    /**
     * Accept a {@link CharSequence} (e.g. {@link String}) as a sequence of integer values.
     *
     * @param   cs          the {@link CharSequence}
     */
    default void accept(CharSequence cs) {
        for (int i = 0, n = cs.length(); i < n; i++)
            accept(cs.charAt(i));
    }

    /**
     * Accept a section of a {@code char} array as a sequence of integer values.
     *
     * @param   chars       the {@code char} array
     * @param   offset      the starting offset
     * @param   length      the length to accept
     */
    default void accept(char[] chars, int offset, int length) {
        for (int i = offset, n = offset + length; i < n; i++)
            accept(chars[i]);
    }

    /**
     * Accept a {@code char} array as a sequence of integer values.
     *
     * @param   chars       the {@code char} array
     */
    default void accept(char[] chars) {
        accept(chars, 0, chars.length);
    }

    /**
     * Accept a section of a {@code byte} array as a sequence of integer values.
     *
     * @param   bytes       the {@code byte} array
     * @param   offset      the starting offset
     * @param   length      the length to accept
     */
    default void accept(byte[] bytes, int offset, int length) {
        for (int i = offset, n = offset + length; i < n; i++)
            accept(bytes[i] & 0xFF);
    }

    /**
     * Accept a {@code byte} array as a sequence of integer values.
     *
     * @param   bytes       the {@code byte} array
     */
    default void accept(byte[] bytes) {
        accept(bytes, 0, bytes.length);
    }

    /**
     * Accept an {@link InputStream} as a sequence of integer values.
     *
     * @param   inputStream     the {@link InputStream}
     * @throws  IOException     if thrown by the {@link InputStream}
     */
    default void accept(InputStream inputStream) throws IOException {
        for (;;) {
            int b = inputStream.read();
            if (b < 0)
                break;
            accept(b & 0xFF);
        }
    }

    /**
     * Accept a {@link Reader} as a sequence of integer values.
     *
     * @param   reader          the {@link InputStream}
     * @throws  IOException     if thrown by the {@link InputStream}
     */
    default void accept(Reader reader) throws IOException {
        for (;;) {
            int ch = reader.read();
            if (ch < 0)
                break;
            accept(ch);
        }
    }

    /**
     * Accept a {@link CharBuffer} as a sequence of integer values.
     *
     * @param   charBuffer      the {@link CharBuffer}
     */
    default void accept(CharBuffer charBuffer) {
        while (charBuffer.hasRemaining())
            accept(charBuffer.get());
    }

    /**
     * Accept a {@link ByteBuffer} as a sequence of integer values.
     *
     * @param   byteBuffer      the {@link ByteBuffer}
     */
    default void accept(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining())
            accept(byteBuffer.get() & 0xFF);
    }

}
