/*
 * @(#) DecoderBase.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2023 Peter Wall
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

package io.jstuff.pipeline.xxml;

import io.jstuff.pipeline.AbstractIntPipeline;
import io.jstuff.pipeline.IntAcceptor;

/**
 * Base class for XML and HTML decoders.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class DecoderBase<R> extends AbstractIntPipeline<R> {

    enum State { NORMAL, AMPERSAND, CHARS, HASH, DIGITS, HEX }

    private final MappingEntry[] table;
    private final StringBuilder sb;
    private int number;
    private State state;

    public DecoderBase(MappingEntry[] table, IntAcceptor<? extends R> downstream) {
        super(downstream);
        this.table = table;
        sb = new StringBuilder(4);
        state = State.NORMAL;
    }

    @Override
    public void acceptInt(int value) {
        switch (state) {
            case NORMAL:
                if (value == '&')
                    state = State.AMPERSAND;
                else
                    emit(value);
                break;
            case AMPERSAND:
                if (value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z') {
                    sb.setLength(0);
                    sb.append((char)value);
                    state = State.CHARS;
                }
                else if (value == '#') {
                    number = 0;
                    state = State.HASH;
                }
                else
                    throw new IllegalArgumentException("Illegal escape sequence");
                break;
            case CHARS:
                if ((value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z') && sb.length() < 12)
                    sb.append((char)value);
                else if (value == ';') {
                    state = State.NORMAL;
                    String entity = sb.toString();
                    int lo = 0;
                    int hi = table.length;
                    while (lo < hi) {
                        int mid = (lo + hi) >>>1;
                        MappingEntry entry = table[mid];
                        String entryString = entry.getString();
                        if (entity.equals(entryString)) {
                            emit(entry.getCodePoint());
                            return;
                        }
                        if (entity.compareTo(entryString) < 0)
                            hi = mid;
                        else
                            lo = mid + 1;
                    }
                    throw new IllegalArgumentException("Illegal escape sequence");
                }
                else
                    throw new IllegalArgumentException("Illegal escape sequence");
                break;
            case HASH:
                if (value >= '0' && value <= '9') {
                    number = value - '0';
                    state = State.DIGITS;
                }
                else if (value == 'x') {
                    number = 0;
                    state = State.HEX;
                }
                else
                    throw new IllegalArgumentException("Illegal escape sequence");
                break;
            case DIGITS:
                if (value >= '0' && value <= '9' && number < 9999999)
                    number = number * 10 + value - '0';
                else if (value == ';') {
                    emit(number);
                    state = State.NORMAL;
                }
                else
                    throw new IllegalArgumentException("Illegal escape sequence");
                break;
            case HEX:
                if (value >= '0' && value <= '9' && number < 0xFFFFFF)
                    number = (number << 4) | value - '0';
                else if (value >= 'A' && value <= 'F')
                    number = (number << 4) | value - 'A' + 10;
                else if (value >= 'a' && value <= 'f')
                    number = (number << 4) | value - 'a' + 10;
                else if (value == ';') {
                    emit(number);
                    state = State.NORMAL;
                }
                else
                    throw new IllegalArgumentException("Illegal escape sequence");
                break;
        }
    }

}
