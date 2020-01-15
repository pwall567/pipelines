/*
 * @(#) IntAcceptor.java
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

package net.pwall.util.pipeline;

/**
 * An acceptor that takes an integer value.  Includes default functions to cater for the common cases of strings or byte
 * arrays being used for integer values.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public interface IntAcceptor<R> extends BaseAcceptor<R> {

    /**
     * Accept a value.
     *
     * @param   value       the value to be processed
     * @throws  Exception   if thrown by a {@code close()} method
     */
    void accept(int value) throws Exception;

    /**
     * Accept a {@link CharSequence} (e.g. {@link String}) as a sequence of integer values.
     *
     * @param   cs      the {@link CharSequence}
     * @throws  Exception if thrown by a {@code close()} method
     */
    default void accept(CharSequence cs) throws Exception {
        for (int i = 0, n = cs.length(); i < n; i++)
            accept(cs.charAt(i));
        close();
    }

    /**
     * Accept a {@code byte} array as a sequence of integer values.
     *
     * @param   bytes   the {@code byte} array
     * @throws  Exception if thrown by a {@code close()} method
     */
    default void accept(byte[] bytes) throws Exception {
        for (byte b : bytes)
            accept(b & 0xFF);
        close();
    }

}
