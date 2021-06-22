/*
 * @(#) Acceptor.java
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * An acceptor that takes a value of the specified type.
 *
 * @author  Peter Wall
 * @param   <A>     the accepted (input) value type
 * @param   <R>     the result type
 */
public interface Acceptor<A, R> extends BaseAcceptor<R> {

    /**
     * Accept a value.
     *
     * @param   value       the value to be processed
     * @throws  Exception   if thrown by a {@code close()} method
     */
    void accept(A value) throws Exception;

    default void accept(Iterator<A> iterator) throws Exception {
        while (iterator.hasNext())
            accept(iterator.next());
        close();
    }

    default void accept(Enumeration<A> enumeration) throws Exception {
        while (enumeration.hasMoreElements())
            accept(enumeration.nextElement());
        close();
    }

    default void accept(Iterable<A> iterable) throws Exception {
        accept(iterable.iterator());
    }

    default void accept(Stream<A> stream) throws Exception {
        accept(stream.iterator());
    }

}
