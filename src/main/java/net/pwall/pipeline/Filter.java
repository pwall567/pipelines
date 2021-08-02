/*
 * @(#) Filter.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021 Peter Wall
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

import java.util.function.Predicate;

/**
 * A pipeline that filters objects based on a specified predicate.
 *
 * @author  Peter Wall
 * @param   <A>     the accepted (input) value type (the emitted type is the same)
 * @param   <R>     the result type
 */
public class Filter<A, R> extends AbstractPipeline<A, A, R> {

    private final Predicate<A> predicate;

    /**
     * Construct a {@code Filter} with the given downstream {@link Acceptor} and {@link Predicate}.
     *
     * @param   downstream  the {@link Acceptor}
     * @param   predicate   the {@link Predicate}
     */
    public Filter(Acceptor<? super A, ? extends R> downstream, Predicate<A> predicate) {
        super(downstream);
        this.predicate = predicate;
    }

    /**
     * Accept a value, after {@code closed} check and test for end of data.  Emit the value if the predicate returns
     * {@code true} for this value.
     *
     * @param   value       the input value
     * @throws  Exception   if thrown by a downstream method
     */
    @Override
    public void acceptObject(A value) throws Exception {
        if (predicate.test(value))
            emit(value);
    }

}
