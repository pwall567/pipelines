/*
 * @(#) AbstractPipeline.java
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
 * Abstract implementation of {@link Pipeline}.
 *
 * @author  Peter Wall
 * @param   <A>     the accepted (input) value type
 * @param   <E>     the emitted (downstream) value type
 * @param   <R>     the result type
 */
abstract public class AbstractPipeline<A, E, R> extends AbstractAcceptor<A, R> implements Pipeline<A, E, R> {

    private final Acceptor<? super E, ? extends R> downstream;

    /**
     * Construct an {@code AbstractPipeline} with the given downstream {@link Acceptor}.
     *
     * @param   downstream  the {@link Acceptor}
     */
    public AbstractPipeline(Acceptor<? super E, ? extends R> downstream) {
        this.downstream = downstream;
    }

    public Acceptor<? super E, ? extends R> getDownstream() {
        return downstream;
    }

    /**
     * Close the pipeline.
     */
    @Override
    public void close() throws Exception {
        downstream.close();
        super.close();
    }

    /**
     * Emit a value to the downstream {@link IntAcceptor}.
     *
     * @param   value   the value to be forwarded
     */
    @Override
    public void emit(E value) throws Exception {
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
