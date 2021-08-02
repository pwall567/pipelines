/*
 * @(#) ByteBufferPipeline.java
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

package net.pwall.pipeline.buffer;

import java.nio.ByteBuffer;

import net.pwall.pipeline.AbstractObjectIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * A pipeline that takes {@link ByteBuffer}s and emits each {@code byte} as an {@code int}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public class ByteBufferPipeline<R> extends AbstractObjectIntPipeline<ByteBuffer, R> {

    /**
     * Construct a {@code ByteBufferPipeline} with the given downstream {@link IntAcceptor}.
     *
     * @param   downstream  the {@link IntAcceptor}
     */
    public ByteBufferPipeline(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    /**
     * Accept a {@link ByteBuffer}, after {@code closed} check and test for end of data.  Emit each byte to the
     * downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     * @throws  Exception   if thrown by the downstream {@link IntAcceptor}
     */
    @Override
    public void acceptObject(ByteBuffer value) throws Exception {
        while (value.hasRemaining())
            emit(value.get());
    }

}
