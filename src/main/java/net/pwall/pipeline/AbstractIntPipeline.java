/*
 * @(#) AbstractIntPipeline.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2021, 2022 Peter Wall
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
 * Abstract base class for {@link IntPipeline} implementations.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public abstract class AbstractIntPipeline<R> extends AbstractIntAcceptor<R> implements IntPipeline<R> {

    private final IntAcceptor<? extends R> downstream;

    /**
     * Construct an {@code AbstractIntPipeline} with the given downstream {@link IntAcceptor}.
     *
     * @param   downstream  the {@link IntAcceptor}
     */
    protected AbstractIntPipeline(IntAcceptor<? extends R> downstream) {
        this.downstream = downstream;
    }

    @Override
    public IntAcceptor<? extends R> getDownstream() {
        return downstream;
    }

    /**
     * Close the pipeline.
     *
     * @throws  Exception   if thrown by the downstream function
     */
    @Override
    public void close() throws Exception {
        downstream.close();
        super.close();
    }

    /**
     * Emit a value to the downstream {@link IntAcceptor}.
     *
     * @param   value       the value to be forwarded
     * @throws  Exception   if thrown by the downstream function
     */
    @Override
    public void emit(int value) throws Exception {
        downstream.accept(value);
    }

    /**
     * Propagate the flush operation to the downstream acceptor.
     */
    @Override
    public void flush() {
        downstream.flush();
    }

}
