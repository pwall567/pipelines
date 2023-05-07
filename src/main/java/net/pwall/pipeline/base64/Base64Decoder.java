/*
 * @(#) Base64Decoder.java
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

import net.pwall.pipeline.AbstractIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * Base64 decoder - decode text encoded in Base 64.  Accepts either conventional encoding or URL-safe encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class Base64Decoder<R> extends AbstractIntPipeline<R> {

    enum State { FIRST, SECOND, THIRD, FOURTH, EQUALS_EXPECTED, COMPLETE }

    private static final int INVALID_MARKER = 127;
    private static final int EQUALS_SIGN_MARKER = 126;
    private static final byte[] decodingArray = new byte[128];

    static {
        for (int i = 0; i < 128; i++)
            decodingArray[i] = 127;

        for (int i = 0; i < 26; i++)
            decodingArray['A' + i] = (byte)i;

        for (int i = 0; i < 26; i++)
            decodingArray['a' + i] = (byte)(i + 26);

        for (int i = 0; i < 10; i++)
            decodingArray['0' + i] = (byte)(i + 52);

        decodingArray['+'] = (byte)62;
        decodingArray['-'] = (byte)62;

        decodingArray['/'] = (byte)63;
        decodingArray['_'] = (byte)63;
        decodingArray[','] = (byte)63; // RFC 3501

        decodingArray['='] = (byte)126;
    }

    private State state;
    private int saved;

    public Base64Decoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
        state = State.FIRST;
    }

    @Override
    public void acceptInt(int value) {
        if ((value & ~0x7F) != 0)
            throw new RuntimeException("Illegal character in Base64");
        int decoded = decodingArray[value];
        if (decoded == INVALID_MARKER)
            throw new RuntimeException("Illegal character in Base64");
        switch (state) {
            case FIRST:
                if (decoded == EQUALS_SIGN_MARKER)
                    throw new RuntimeException("Misplaced '=' sign");
                saved = decoded;
                state = State.SECOND;
                break;
            case SECOND:
                if (decoded == EQUALS_SIGN_MARKER)
                    throw new RuntimeException("Misplaced '=' sign");
                emit(((saved << 2) & 0xFC) | ((decoded >> 4) & 0x03));
                saved = decoded;
                state = State.THIRD;
                break;
            case THIRD:
                if (decoded == EQUALS_SIGN_MARKER) {
                    if ((saved & 0x0F) != 0)
                        throw new RuntimeException("Misplaced '=' sign");
                    state = State.EQUALS_EXPECTED;
                }
                else {
                    emit(((saved << 4) & 0xF0) | ((decoded >> 2) & 0x0F));
                    saved = decoded;
                    state = State.FOURTH;
                }
                break;
            case FOURTH:
                if (decoded == EQUALS_SIGN_MARKER) {
                    if ((saved & 0x03) != 0)
                        throw new RuntimeException("Misplaced '=' sign");
                    state = State.COMPLETE;
                }
                else {
                    emit(((saved << 6) & 0xC0) | decoded);
                    state = State.FIRST;
                }
                break;
            case EQUALS_EXPECTED:
                if (decoded != EQUALS_SIGN_MARKER)
                    throw new RuntimeException("Misplaced '=' sign");
                state = State.COMPLETE;
                break;
            case COMPLETE:
                throw new RuntimeException("Illegal characters after end of Base64");
        }
    }

    @Override
    public boolean isStageComplete() {
        return state == State.FIRST || state == State.COMPLETE || state == State.THIRD && (saved & 0x0F) == 0 ||
                state == State.FOURTH && (saved & 0x03) == 0;
    }

}
