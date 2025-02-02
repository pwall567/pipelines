/*
 * @(#) DecodingPipeline.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2023 Peter Wall
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

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.IntPipeline;

/**
 * An {@link IntPipeline} to convert one-to-one mapping character encodings to Unicode code points.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class DecodingPipeline<R> extends ErrorStrategyBase<R> {

    private final String table;

    public DecodingPipeline(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy, String table) {
        super(downstream, errorStrategy);
        this.table = table;
    }

    @Override
    public void acceptInt(int value) {
        if (value >= 0 && value <= 0x7F)
            emit(value);
        else if (value >= 0x80 && value <= 0xFF)
            emit(table.charAt(value - 0x80));
        else
            handleError(value);
    }

}
