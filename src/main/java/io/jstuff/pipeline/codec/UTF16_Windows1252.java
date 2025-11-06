/*
 * @(#) UTF16_Windows1252.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2023, 2025 Peter Wall
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
 * An encoder {@link IntPipeline} to convert UTF-16 data to Windows-1252 encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class UTF16_Windows1252<R> extends EncodingPipeline<R> {

    public UTF16_Windows1252(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy, createReverseTable(Windows1252_UTF16.table));
    }

    public UTF16_Windows1252(IntAcceptor<? extends R> downstream) {
        super(downstream, ErrorStrategy.THROW_EXCEPTION, createReverseTable(Windows1252_UTF16.table));
    }

    /**
     * Convert a {@code String} to a byte array using the {@code UTF16_Windows1252} converter.
     *
     * @param   input           the input as a {@code String}
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a byte array
     */
    public static byte[] convert(String input, ErrorStrategy errorStrategy) {
        IntPipeline<byte[]> pipe = new UTF16_Windows1252<>(new ByteArrayAcceptor(), errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a {@code String} to a byte array using the {@code UTF16_Windows1252} converter.
     *
     * @param   input   the input as a {@code String}
     * @return          the converted data as a byte array
     */
    public static byte[] convert(String input) {
        return convert(input, ErrorStrategy.THROW_EXCEPTION);
    }

}
