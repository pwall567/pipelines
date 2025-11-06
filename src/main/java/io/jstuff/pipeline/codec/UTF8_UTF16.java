/*
 * @(#) UTF8_UTF16.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2021, 2023, 2025 Peter Wall
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

import java.util.function.IntConsumer;

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.StringAcceptor;

/**
 * A decoder {@link IntPipeline} to convert UTF-8 to UTF-16.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class UTF8_UTF16<R> extends ErrorHandlingIntPipeline<R> {

    private final IntConsumer threeByte1 = i -> intermediate(i, this::terminal);
    private final IntConsumer fourByte2 = i -> intermediate(i, this::terminal);
    private final IntConsumer fourByte1 = i -> intermediate(i, fourByte2);
    private final IntConsumer normal = i -> {
        if (i == -1 || (i & 0x80) == 0)
            emit(i);
        else if ((i & 0x40) == 0)
            handleError(i);
        else if ((i & 0x20) == 0)
            startSequence(i & 0x1F, this::terminal);
        else if ((i & 0x10) == 0)
            startSequence(i & 0x0F, threeByte1);
        else if ((i & 0x08) == 0)
            startSequence(i & 0x07, fourByte1);
        else
            handleError(i);
    };

    private IntConsumer state;
    private int codePoint;

    public UTF8_UTF16(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
        state = normal;
    }

    public UTF8_UTF16(IntAcceptor<? extends R> downstream) {
        super(downstream, ErrorStrategy.THROW_EXCEPTION);
        state = normal;
    }

    @Override
    public void acceptInt(int value) {
        state.accept(value);
    }

    @Override
    public boolean isStageComplete() {
        return state == normal;
    }

    private void startSequence(int i, IntConsumer nextState) {
        codePoint = i;
        state = nextState;
    }

    private void intermediate(int i, IntConsumer nextState) {
        if ((i & 0xC0) == 0x80) {
            codePoint = (codePoint << 6) | (i & 0x3F);
            state = nextState;
        }
        else {
            handleError(i);
            state = normal;
        }
    }

    private void terminal(int i) {
        if ((i & 0xC0) == 0x80) {
            int c = (codePoint << 6) | (i & 0x3F);
            if (Character.isBmpCodePoint(c))
                emit(c);
            else {
                emit(Character.highSurrogate(c));
                emit(Character.lowSurrogate(c));
            }
        }
        else
            handleError(i);
        state = normal;
    }

    /**
     * Convert a byte array to a {@code String} using the {@code UTF8_UTF16} converter.
     *
     * @param   input           the input as a byte array
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static String convert(byte[] input, ErrorStrategy errorStrategy) {
        IntPipeline<String> pipe = new UTF8_UTF16<>(new StringAcceptor(), errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a byte array to a {@code String} using the {@code UTF8_UTF16} converter.
     *
     * @param   input   the input as a byte array
     * @return          the converted data as a {@code String}
     */
    public static String convert(byte[] input) {
        return convert(input, ErrorStrategy.THROW_EXCEPTION);
    }

}
