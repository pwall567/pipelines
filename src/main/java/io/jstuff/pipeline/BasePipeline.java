/*
 * @(#) BasePipeline.java
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

package io.jstuff.pipeline;

/**
 * Base interface for pipeline classes.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public interface BasePipeline<R> extends BaseAcceptor<R> {

    /**
     * Get the downstream {@link BaseAcceptor}.
     *
     * @return      the {@link BaseAcceptor}
     */
    BaseAcceptor<? extends R> getDownstream();

    /**
     * Return {@code true} if all sequences in the input to this stage of the pipeline are complete, that is, the input
     * is not in the middle of a sequence requiring more data.  This should be overridden by pipeline stages that
     * process complex sequences of input.
     *
     * @return  {@code true} if the input is in the "complete" state
     */
    default boolean isStageComplete() {
        return true;
    }

    /**
     * Return {@code true} if all sequences in the input are complete, both for the current stage and for the downstream
     * stages.
     *
     * @return  {@code true} if the input is in the "complete" state
     */
    @Override
    default boolean isComplete() {
        return isStageComplete() && getDownstream().isComplete();
    }

    /**
     * Get the result object (defaults to the result of the downstream acceptor).
     *
     * @return  the result
     * @throws  IllegalStateException   if there is an incomplete sequence in progress
     */
    @Override
    default R getResult() {
        if (!isComplete())
            throw new IllegalStateException("Sequence is not complete");
        return getDownstream().getResult();
    }

}
