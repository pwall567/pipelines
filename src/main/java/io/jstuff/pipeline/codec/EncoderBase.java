/*
 * @(#) EncoderBase.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2022, 2023 Peter Wall
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

package io.jstuff.pipeline.codec;

import io.jstuff.util.IntOutput;

import io.jstuff.pipeline.AbstractIntPipeline;
import io.jstuff.pipeline.IntAcceptor;

/**
 * Base class for encoders.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public abstract class EncoderBase<R> extends AbstractIntPipeline<R> {

    protected EncoderBase(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    /**
     * Emit a string.
     *
     * @param   string      the string
     */
    protected void emit(String string) {
        for (int i = 0, n = string.length(); i < n; i++)
            emit(string.charAt(i));
    }

    /**
     * Emit an integer as hexadecimal digits.
     *
     * @param   i       the integer
     */
    protected void emitHex(int i) {
        IntOutput.outputIntHex(i, this::emit);
    }

}
