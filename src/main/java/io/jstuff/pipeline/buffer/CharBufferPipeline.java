/*
 * @(#) CharBufferPipeline.java
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

import java.nio.CharBuffer;

import io.jstuff.pipeline.AbstractObjectIntPipeline;
import io.jstuff.pipeline.IntAcceptor;

/**
 * A pipeline that takes {@link CharBuffer}s and emits each character as an {@code int}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public class CharBufferPipeline<R> extends AbstractObjectIntPipeline<CharBuffer, R> {

    /**
     * Construct a {@code ByteBufferPipeline} with the given downstream {@link IntAcceptor}.
     *
     * @param downstream the {@link IntAcceptor}
     */
    public CharBufferPipeline(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    /**
     * Accept a {@link CharBuffer}, after {@code closed} check and test for end of data.  Emit each byte to the
     * downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     */
    @Override
    public void acceptObject(CharBuffer value) {
        while (value.hasRemaining())
            emit(value.get());
    }

}
