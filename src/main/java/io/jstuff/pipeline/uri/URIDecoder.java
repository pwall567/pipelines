/*
 * @(#) URIDecoder.java
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

package io.jstuff.pipeline.uri;

import java.util.List;

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.ListIntAcceptor;
import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.ErrorStrategy;
import io.jstuff.pipeline.codec.ErrorHandlingIntPipeline;

/**
 * URI decoder - decode text using URI percent-encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class URIDecoder<R> extends ErrorHandlingIntPipeline<R> {

    enum State { NORMAL, FIRST, SECOND }

    private State state;
    private int character;

    public URIDecoder(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
        state = State.NORMAL;
    }

    public URIDecoder(IntAcceptor<? extends R> downstream) {
        this(downstream, ErrorStrategy.DEFAULT);
    }

    @Override
    public void acceptInt(int value) {
        switch (state) {
        case NORMAL:
            if (value == '%')
                state = State.FIRST;
            else if (value == '+')
                emit(' ');
            else
                emit(value);
            break;
        case FIRST:
            character = fromHex(value) << 4;
            state = State.SECOND;
            break;
        case SECOND:
            emit(character | fromHex(value));
            state = State.NORMAL;
            break;
        }
    }

    private int fromHex(int ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'Z')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'z')
            return ch - 'a' + 10;
        throw new IllegalArgumentException("Illegal hex character - " + (char)ch);
    }

    /**
     * Convert a {@code String} using the {@code URIDecoder} converter.
     *
     * @param   input   the input as a {@code String}
     * @return          the converted data as a {@code String}
     */
    public static String convert(String input) {
        IntPipeline<String> pipe = new URIDecoder<>(new StringAcceptor());
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a {@code List<Integer>} (Unicode code points) using the {@code URIDecoder} converter.
     *
     * @param   input   the input as a {@code String}
     * @return          the converted data as a {@code String}
     */
    public static List<Integer> convert(List<Integer> input) {
        IntPipeline<List<Integer>> pipe = new URIDecoder<>(new ListIntAcceptor());
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

}
