/*
 * @(#) AbstractIntPipeline.java
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

import java.util.function.IntConsumer;

/**
 * Abstract base class for {@link IntPipeline} implementations.  Includes functionality for end of data testing, and
 * forwarding to a downstream {@link IntConsumer}.
 *
 * @author  Peter Wall
 */
public abstract class AbstractIntPipeline implements IntPipeline {

    private IntConsumer downstream;
    private boolean closed;

    /**
     * Construct an {@code AbstractIntPipeline} with the given downstream {@link IntConsumer}.
     *
     * @param   downstream  the {@link IntConsumer}
     */
    protected AbstractIntPipeline(IntConsumer downstream) {
        this.downstream = downstream;
        closed = false;
    }

    /**
     * Accept an {@code int}.  Check for pipeline already closed, and handles end of data.
     *
     * @param   value   the input value
     */
    @Override
    public void accept(int value) {
        if (isClosed())
            throw new IllegalStateException("Pipeline is closed");
        if (value == END_OF_DATA) {
            if (!isComplete())
                throw new IllegalStateException("Unexpected end of data");
            close();
        }
        else
            internalAccept(value);
    }

    /**
     * Return {@code true} if all sequences in the pipeline are complete, that is, the pipeline is not in the middle of
     * a sequence requiring more data.
     *
     * @return  {@code true} if the pipeline is in the "complete" state
     */
    @Override
    public boolean isComplete() {
        return true;
    }

    /**
     * Return {@code true} if the pipeline is closed, that is, an end of data marker has been received.
     *
     * @return  {@code true} if the pipeline is closed
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * Close the pipeline.
     */
    public void close() {
        setClosed(true);
        forward(END_OF_DATA);
    }

    /**
     * Set the {@code closed} state of the pipeline.
     *
     * @param   closed  the new {@code closed} state
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * Forward a value to the downstream {@link IntConsumer}.
     *
     * @param   value   the value to be forwarded
     */
    protected void forward(int value) {
        downstream.accept(value);
    }

    /**
     * Accept an {@code int}, after {@code closed} check and test for end of data.  Implementing classes must supply an
     * implementation of this method.
     *
     * @param   value   the input value
     */
    protected abstract void internalAccept(int value);

}
