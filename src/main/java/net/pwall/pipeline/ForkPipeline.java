/*
 * @(#) ForkPipeline.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2023 Peter Wall
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

public class ForkPipeline<A, R> extends AbstractPipeline<A, A, R> {

    private final Acceptor<? super A, ? extends R> downstream2;

    /**
     * Construct a {@code Fork} with the given downstream {@link Acceptor}.
     *
     * @param downstream1 the {@link Acceptor}
     * @param downstream2 the {@link Acceptor}
     */
    public ForkPipeline(Acceptor<? super A, ? extends R> downstream1, Acceptor<? super A, ? extends R> downstream2) {
        super(downstream1);
        this.downstream2 = downstream2;
    }

    @Override
    public void acceptObject(A value) {
        emit(value);
        downstream2.accept(value);
    }

}
