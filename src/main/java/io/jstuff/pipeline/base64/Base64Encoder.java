/*
 * @(#) Base64Encoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2023 Peter Wall
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

package io.jstuff.pipeline.base64;

import java.util.List;

import io.jstuff.pipeline.ByteArrayAcceptor;
import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.ListIntAcceptor;
import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.ErrorStrategy;
import io.jstuff.pipeline.codec.ErrorHandlingIntPipeline;

/**
 * Base64 encoder - encode bytes using Base 64.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class Base64Encoder<R> extends ErrorHandlingIntPipeline<R> {

    public enum State { FIRST, SECOND, THIRD }

    public static final byte[] encodingArrayMain = new byte[64];
    public static final byte[] encodingArrayURL = new byte[64];

    static {
        for (int i = 0; i < 26; i++) {
            encodingArrayMain[i] = (byte)('A' + i);
            encodingArrayURL[i] = (byte)('A' + i);
        }

        for (int i = 0; i < 26; i++) {
            encodingArrayMain[i + 26] = (byte)('a' + i);
            encodingArrayURL[i + 26] = (byte)('a' + i);
        }

        for (int i = 0; i < 10; i++) {
            encodingArrayMain[i + 52] = (byte)('0' + i);
            encodingArrayURL[i + 52] = (byte)('0' + i);
        }

        encodingArrayMain[62] = '+';
        encodingArrayURL[62] = '-';

        encodingArrayMain[63] = '/';
        encodingArrayURL[63] = '_';
    }

    private final byte[] encodingArray;
    private final boolean urlSafe;
    private State state;
    private int saved;

    public Base64Encoder(IntAcceptor<? extends R> downstream, boolean urlSafe, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
        state = State.FIRST;
        encodingArray = urlSafe ? encodingArrayURL : encodingArrayMain;
        this.urlSafe = urlSafe;
    }

    public Base64Encoder(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        this(downstream, false, errorStrategy);
    }

    public Base64Encoder(IntAcceptor<? extends R> downstream, boolean urlSafe) {
        this(downstream, urlSafe, ErrorStrategy.DEFAULT);
    }

    public Base64Encoder(IntAcceptor<? extends R> downstream) {
        this(downstream, false, ErrorStrategy.DEFAULT);
    }

    @Override
    public void acceptInt(int value) {
        if (value > 0xFF) {
            handleError(value);
            return;
        }
        switch (state) {
            case FIRST:
                emit(encodingArray[(value >> 2) & 0x3F]);
                saved = value;
                state = State.SECOND;
                break;
            case SECOND:
                emit(encodingArray[((saved << 4) & 0x30) | ((value >> 4) & 0x0F)]);
                saved = value;
                state = State.THIRD;
                break;
            case THIRD:
                emit(encodingArray[((saved << 2) & 0x3C) | ((value >> 6) & 0x03)]);
                emit(encodingArray[value & 0x3F]);
                state = State.FIRST;
                break;
        }
    }

    @Override
    public void close() {
        switch (state) {
            case FIRST:
                break;
            case SECOND:
                emit(encodingArray[(saved << 4) & 0x30]);
                if (!urlSafe) {
                    emit('=');
                    emit('=');
                }
                break;
            case THIRD:
                emit(encodingArray[(saved << 2) & 0x3C]);
                if (!urlSafe)
                    emit('=');
                break;
        }
    }

    /**
     * Convert a byte array using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a byte array
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static byte[] convert(byte[] input, boolean urlSafe, ErrorStrategy errorStrategy) {
        IntPipeline<byte[]> pipe = new Base64Encoder<>(new ByteArrayAcceptor(), urlSafe, errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a byte array using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a byte array
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static byte[] convert(byte[] input, ErrorStrategy errorStrategy) {
        return convert(input, false, errorStrategy);
    }

    /**
     * Convert a byte array using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a byte array
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @return                  the converted data as a {@code String}
     */
    public static byte[] convert(byte[] input, boolean urlSafe) {
        return convert(input, urlSafe, ErrorStrategy.THROW_EXCEPTION);
    }

    /**
     * Convert a byte array using the {@code Base64Encoder} converter.
     *
     * @param   input   the input as a byte array
     * @return          the converted data as a {@code String}
     */
    public static byte[] convert(byte[] input) {
        return convert(input, false, ErrorStrategy.THROW_EXCEPTION);
    }

    /**
     * Convert a {@code String} using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code String}
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static String convert(String input, boolean urlSafe, ErrorStrategy errorStrategy) {
        IntPipeline<String> pipe = new Base64Encoder<>(new StringAcceptor(), urlSafe, errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a {@code String} using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code String}
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static String convert(String input, ErrorStrategy errorStrategy) {
        return convert(input, false, errorStrategy);
    }

    /**
     * Convert a {@code String} using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code String}
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @return                  the converted data as a {@code String}
     */
    public static String convert(String input, boolean urlSafe) {
        return convert(input, urlSafe, ErrorStrategy.THROW_EXCEPTION);
    }

    /**
     * Convert a {@code String} using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code String}
     * @return                  the converted data as a {@code String}
     */
    public static String convert(String input) {
        return convert(input, false, ErrorStrategy.THROW_EXCEPTION);
    }

    /**
     * Convert a {@code List<Integer>} (Unicode code points) using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code List<Integer>}
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static List<Integer> convert(List<Integer> input, boolean urlSafe, ErrorStrategy errorStrategy) {
        IntPipeline<List<Integer>> pipe = new Base64Encoder<>(new ListIntAcceptor(), urlSafe, errorStrategy);
        pipe.accept(input);
        pipe.safeClose();
        return pipe.getResult();
    }

    /**
     * Convert a {@code List<Integer>} (Unicode code points) using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code List<Integer>}
     * @param   errorStrategy   the {@link ErrorStrategy}
     * @return                  the converted data as a {@code String}
     */
    public static List<Integer> convert(List<Integer> input, ErrorStrategy errorStrategy) {
        return convert(input, false, errorStrategy);
    }

    /**
     * Convert a {@code List<Integer>} (Unicode code points) using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code List<Integer>}
     * @param   urlSafe         {@code true} to use the URL-safe encoding characters
     * @return                  the converted data as a {@code String}
     */
    public static List<Integer> convert(List<Integer> input, boolean urlSafe) {
        return convert(input, urlSafe, ErrorStrategy.THROW_EXCEPTION);
    }

    /**
     * Convert a {@code List<Integer>} (Unicode code points) using the {@code Base64Encoder} converter.
     *
     * @param   input           the input as a {@code List<Integer>}
     * @return                  the converted data as a {@code String}
     */
    public static List<Integer> convert(List<Integer> input) {
        return convert(input, false, ErrorStrategy.THROW_EXCEPTION);
    }

}
