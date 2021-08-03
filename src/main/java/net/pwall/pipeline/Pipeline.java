/*
 * @(#) Pipeline.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2021 Peter Wall
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

package net.pwall.pipeline;

/**
 * A pipeline that accepts and emits values of the specified types.
 *
 * @author  Peter Wall
 * @param   <A>     the accepted (input) value type
 * @param   <E>     the emitted (downstream) value type
 * @param   <R>     the result type
 */
public interface Pipeline<A, E, R> extends Acceptor<A, R>, BasePipeline<R> {

    /**
     * Emit a value, that is, forward a value to the downstream acceptor.
     *
     * @param   value   the value
     * @throws  Exception if thrown by a {@code close()} method
     */
    void emit(E value) throws Exception;

}
