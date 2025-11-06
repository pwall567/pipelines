/*
 * @(#) UTF16_ASCII.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2023, 2025 Peter Wall
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

import io.jstuff.pipeline.ByteArrayAcceptor;
import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.IntPipeline;

/**
 * An encoder {@link IntPipeline} to convert UTF-16 data to ASCII encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class UTF16_ASCII<R> extends ErrorHandlingIntPipeline<R> {

    public UTF16_ASCII(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
    }

    public UTF16_ASCII(IntAcceptor<? extends R> downstream) {
        super(downstream, ErrorStrategy.THROW_EXCEPTION);
    }

    @Override
    public void acceptInt(int value) {
        if (value >= 0 && value <= 0x7F)
            emit(value);
        else
            throw new IllegalArgumentException("Illegal character");
    }

    /**
     * Convert a {@code String} to a byte array using the {@code UTF16_ASCII} converter.
     *
     * @param   input           the input as a {@code String}
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a byte array
     */
    public static byte[] convert(String input, ErrorStrategy errorStrategy) {
        IntPipeline<byte[]> pipe = new UTF16_ASCII<>(new ByteArrayAcceptor(), errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a {@code String} to a byte array using the {@code UTF16_ASCII} converter.
     *
     * @param   input   the input as a {@code String}
     * @return          the converted data as a byte array
     */
    public static byte[] convert(String input) {
        return convert(input, ErrorStrategy.THROW_EXCEPTION);
    }

}
