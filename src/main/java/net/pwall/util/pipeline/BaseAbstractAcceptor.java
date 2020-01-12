/*
 * @(#) BaseAbstractAcceptor.java
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
 * Common functionality for {@link AbstractAcceptor} and {@link AbstractIntAcceptor}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
abstract public class BaseAbstractAcceptor<R> implements BaseAcceptor<R> {

    private boolean closed;

    /**
     * Construct a {@code BaseAbstractAcceptor}.
     */
    public BaseAbstractAcceptor() {
        closed = false;
    }

    /**
     * Return {@code true} if the acceptor is closed, that is, an end of data marker has been received.
     *
     * @return  {@code true} if the acceptor is closed
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * Close the acceptor.  Throws an exception if the acceptor is not in the "complete" state.
     *
     * @throws  Exception   if the acceptor is not in the "complete" state
     */
    @Override
    public void close() throws Exception {
        if (!isComplete())
            throw new IllegalStateException("Sequence not complete");
        closed = true;
    }

}
