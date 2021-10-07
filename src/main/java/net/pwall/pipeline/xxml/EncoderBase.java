/*
 * @(#) EncoderBase.java
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

package net.pwall.pipeline.xxml;

import net.pwall.pipeline.AbstractIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * Base class for XML and HTML encoders.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public abstract class EncoderBase<R> extends AbstractIntPipeline<R> {

    private static final String hexChars = "0123456789ABCDEF";

    public EncoderBase(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    protected void emit(String string) throws Exception {
        for (int i = 0, n = string.length(); i < n; i++)
            emit(string.charAt(i));
    }

    protected void emitHex(int i) throws Exception {
        if (i > 0xF)
            emitHex(i >>> 4);
        emit(hexChars.charAt(i & 0xF));
    }

}
