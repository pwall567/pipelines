/*
 * @(#) ByteArrayPipeline.java
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

package io.jstuff.pipeline.buffer;

import io.jstuff.pipeline.AbstractObjectIntPipeline;
import io.jstuff.pipeline.IntAcceptor;

/**
 * A pipeline that takes {@code byte} arrays and emits each {@code byte} as an {@code int}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public class ByteArrayPipeline<R> extends AbstractObjectIntPipeline<byte[], R> {

    /**
     * Construct a {@code ByteArrayPipeline} with the given downstream {@link IntAcceptor}.
     *
     * @param   downstream  the {@link IntAcceptor}
     */
    public ByteArrayPipeline(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    /**
     * Accept a {@code byte} array, after {@code closed} check and test for end of data.  Emit each byte to the
     * downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     */
    @Override
    public void acceptObject(byte[] value) {
        for (byte b : value)
            emit(b);
    }

    /**
     * Accept a {@code byte} array with the specified offset and length. Check for pipeline already closed, and handle
     * end of data.  Emit each byte to the downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     * @param   offset      the offset from the start of the buffer
     * @param   length      the length (number of bytes) to process
     * @throws  IllegalStateException   if the {@code ByteArrayPipeline} has already been closed
     */
    public void accept(byte[] value, int offset, int length) {
        if (isClosed())
            throw new IllegalStateException("Acceptor is closed");
        if (value == null)
            safeClose();
        else
            acceptObject(value, offset, length);
    }

    /**
     * Accept a {@code byte} array with the specified offset and length, after {@code closed} check and test for end of
     * data.  Emit each byte to the downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     * @param   offset      the offset from the start of the buffer
     * @param   length      the length (number of bytes) to process
     */
    public void acceptObject(byte[] value, int offset, int length) {
        for (int i = offset, n = offset + length; i < n; i++)
            emit(value[i]);
    }

}
