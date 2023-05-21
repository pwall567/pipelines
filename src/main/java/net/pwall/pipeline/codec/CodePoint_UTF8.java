/*
 * @(#) CodePoint_UTF8.java
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

package net.pwall.pipeline.codec;

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.IntPipeline;

/**
 * An encoder {@link IntPipeline} to convert Unicode code points to UTF-8.  Note that this encoder will convert
 * surrogate characters to 3-byte sequences without reporting an error, so it may be used as a UTF-16 to UTF-8 encoder.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class CodePoint_UTF8<R> extends ErrorStrategyBase<R> {

    public CodePoint_UTF8(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
    }

    public CodePoint_UTF8(IntAcceptor<? extends R> downstream) {
        super(downstream, ErrorStrategy.THROW_EXCEPTION);
    }

    @Override
    public void acceptInt(int value) {
        if (value <= 0x7F)
            emit(value);
        else if (value <= 0x7FF) {
            emit(0xC0 | value >> 6);
            emit(0x80 | (value & 0x3F));
        }
        else if (value <= 0xFFFF) {
            emit(0xE0 | value >> 12);
            emit(0x80 | ((value >> 6) & 0x3F));
            emit(0x80 | (value & 0x3F));
        }
        else if (value <= 0x10FFFF) {
            emit(0xF0 | value >> 18);
            emit(0x80 | ((value >> 12) & 0x3F));
            emit(0x80 | ((value >> 6) & 0x3F));
            emit(0x80 | (value & 0x3F));
        }
        else
            handleError(value);
    }

}
