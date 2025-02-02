/*
 * @(#) BaseAcceptor.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2023 Peter Wall
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

/**
 * The base interface for pipeline and acceptor classes.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public interface BaseAcceptor<R> extends AutoCloseable {

    /**
     * Return {@code true} if the pipeline is closed.
     *
     * @return  {@code true} if the pipeline is closed
     */
    boolean isClosed();

    /**
     * Get the result of the acceptor.  The default implementation throws an exception.
     *
     * @return  the result
     * @throws  UnsupportedOperationException   in all cases
     */
    default R getResult() {
        throw new UnsupportedOperationException("No result defined");
    }

    /**
     * Return {@code true} if all sequences in the input are complete, that is, the input is not in the middle of a
     * sequence requiring more data.  This should be overridden by acceptors that process complex sequences of input.
     *
     * @return  {@code true} if the input is in the "complete" state
     */
    default boolean isComplete() {
        return true;
    }

    /**
     * Flush the acceptor (required by some implementations - default is a no-operation).
     */
    default void flush() {
    }

    /**
     * Close the acceptor, reporting any exceptions as a {@link RuntimeException}.  This avoids having to declare
     * exceptions on each method.
     *
     * @throws  RuntimeException    if thrown by {@code close()} method
     */
    default void safeClose() {
        try {
            close();
        }
        catch (RuntimeException re) {
            throw re; // don't wrap it we don't have to
        }
        catch (Exception e) {
            throw new RuntimeException("Exception in close function", e);
        }
    }

}
