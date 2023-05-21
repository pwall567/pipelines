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

package net.pwall.pipeline.base64;

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.codec.ErrorStrategy;
import net.pwall.pipeline.codec.ErrorStrategyBase;

/**
 * Base64 encoder - encode bytes using Base 64.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class Base64Encoder<R> extends ErrorStrategyBase<R> {

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

}
