/*
 * @(#) AbstractIntAcceptor.java
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
 * Abstract implementation of {@link IntAcceptor}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
abstract public class AbstractIntAcceptor<R> extends BaseAbstractAcceptor<R> implements IntAcceptor<R> {

    /**
     * Accept an {@code int}.  Check for pipeline already closed, and handle end of data.
     *
     * @param   value   the input value
     */
    @Override
    public void accept(int value) throws Exception {
        if (isClosed())
            throw new IllegalStateException("Acceptor is closed");
        if (value == -1)
            close();
        else
            acceptInt(value);
    }

    /**
     * Accept an {@code int}, after {@code closed} check and test for end of data.  Implementing classes must supply an
     * implementation of this method.
     *
     * @param   value   the input value
     */
    abstract public void acceptInt(int value) throws Exception;

}
