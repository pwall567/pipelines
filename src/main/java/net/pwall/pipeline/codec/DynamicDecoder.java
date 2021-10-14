/*
 * @(#) DynamicDecoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021 Peter Wall
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

import java.nio.charset.Charset;

import net.pwall.pipeline.IntAcceptor;

/**
 * A dynamic decoding pipeline which attempts to determine the encoding of the data from its content.
 *
 * @author  Peter Wall
 * @param   <R>     the "Result" class (may be {@link Void})
 */
public class DynamicDecoder<R> extends SwitchableDecoder<R> {

    public enum State { INITIAL, FIRST_00, FIRST_FF, FIRST_FE, FIRST_EF, FIRST_TWO_EF_BB,
        FIRST_ANY, FIRST_TWO_ANY_00, FIRST_THREE_ANY_00_00,
        FIRST_TWO_00_00, FIRST_THREE_00_00_FE, FIRST_TWO_FF_FE, FIRST_THREE_FF_FE_00,
        UNDETERMINED, POSSIBLE_UTF8, POSSIBLE_UTF8_3BYTE, POSSIBLE_UTF8_4BYTE, DELEGATED }

    protected State state;
    private final int[] buffer;
    private int index;

    /**
     * Construct a {@code DynamicDecoder} with the specified downstream {@link IntAcceptor}.
     *
     * @param   downstream  the downstream {@link IntAcceptor}
     */
    public DynamicDecoder(IntAcceptor<? extends R> downstream) {
        this(downstream, null);
    }

    /**
     * Construct a {@code DynamicDecoder} with the specified downstream {@link IntAcceptor}, switching immediately to
     * the specified {@link Charset} if specified.  This bypasses much of the functionality of the
     * {@code DynamicDecoder}, but allowing the {@link Charset} to be specified in this way simplifies the creation of a
     * decoder in those cases where the encoding may be, but is not always, known in advance.
     *
     * @param   downstream  the downstream {@link IntAcceptor}
     * @param   charset     the {@link Charset} if known, or {@code null} to allow dynamic encoding determination
     */
    public DynamicDecoder(IntAcceptor<? extends R> downstream, Charset charset) {
        super(downstream);
        buffer = new int[4];
        index = 0;
        if (charset == null)
            state = State.INITIAL;
        else {
            delegate = DecoderFactory.getDecoder(charset, downstream);
            state = State.DELEGATED;
        }
    }

    /**
     * Accept an {@code int} (after {@code closed} check and test for end of data).  This function operates as a "state
     * machine", with much of the complexity involved in the examination of the first four characters of the stream to
     * recognise big-endian and little-endian UTF-16 and UTF-32 encodings.
     *
     * <p>If the stream appears to consist of 8-bit characters, it is placed in an indeterminate state until a character
     * above {@code 0x80} is encountered, at which point the decoder will check whether the subsequent characters are
     * consistent with a UTF-8 encoding sequence, in which case that encoding will be assumed for the remainder of the
     * stream; otherwise the Windows-1252 encoding is assumed.</p>
     *
     * @param   value       the input value
     * @throws  Exception   if thrown by a {@code close()} method
     */
    @Override
    public void acceptInt(int value) throws Exception {
        switch (state) {
        case INITIAL:
            buffer[index++] = value;
            switch (value) {
            case 0:
                state = State.FIRST_00;
                break;
            case 0xFF:
                state = State.FIRST_FF;
                break;
            case 0xFE:
                state = State.FIRST_FE;
                break;
            case 0xEF:
                state = State.FIRST_EF;
                break;
            default:
                state = State.FIRST_ANY;
            }
            break;
        case FIRST_00:
            buffer[index++] = value;
            if (value == 0)
                state = State.FIRST_TWO_00_00;
            else
                switchTo(new UTF16BE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
            break;
        case FIRST_TWO_00_00:
            if (value == 0xFE)
                state = State.FIRST_THREE_00_00_FE;
            else {
                switchTo(new UTF32BE_CodePoint<>(getDownstream()));
                delegate.accept(value);
            }
            break;
        case FIRST_THREE_00_00_FE:
            if (value == 0xFF) {
                switchTo(new UTF32BE_CodePoint<>(getDownstream()));
                delegate.accept(0xFE);
                delegate.accept(value);
            }
            else {
                index = 0;
                switchTo(new UTF32BE_CodePoint<>(getDownstream()));
            }
            break;
        case FIRST_FF:
            if (value == 0xFE) {
                buffer[index++] = value;
                state = State.FIRST_TWO_FF_FE;
            }
            else
                delegateToWindows1252(value);
            break;
        case FIRST_TWO_FF_FE:
            if (value == 0) {
                buffer[index++] = 0;
                state = State.FIRST_THREE_FF_FE_00;
            }
            else {
                index = 0;
                switchTo(new UTF16LE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
                delegate.accept(value);
            }
            break;
        case FIRST_THREE_FF_FE_00:
            if (value == 0) {
                index = 0;
                switchTo(new UTF32LE_CodePoint<>(getDownstream()));
            }
            else {
                index = 0;
                switchTo(new UTF16LE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
                delegate.accept(buffer[2]);
                delegate.accept(value);
            }
            index = 0;
            break;
        case FIRST_FE:
            if (value == 0xFF) {
                index = 0;
                switchTo(new UTF16BE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
            }
            else
                delegateToWindows1252(value);
            break;
        case FIRST_EF:
            if (value == 0xBB) {
                buffer[index++] = value;
                state = State.FIRST_TWO_EF_BB;
            }
            else if ((value & 0xC0) == 0x80)
                delegateToUTF8(value);
            else
                delegateToWindows1252(value);
            break;
        case FIRST_TWO_EF_BB:
            if (value == 0xBF) {
                index = 0;
                switchTo(new UTF8_CodePoint<>(getDownstream()));
            }
            else if ((value & 0xC0) == 0x80)
                delegateToUTF8(value);
            else
                delegateToWindows1252(value);
            break;
        case FIRST_ANY:
            if (value == 0) {
                buffer[index++] = value;
                state = State.FIRST_TWO_ANY_00;
            }
            else {
                int first = buffer[0];
                if (first >= 0x80) {
                    if ((first & 0xE0) == 0xC0) {
                        if ((value & 0xC0) == 0x80)
                            delegateToUTF8(value);
                        else
                            delegateToWindows1252(value);
                    }
                    else if (first >= 0xE0 && first <= 0xF7) {
                        if ((value & 0xC0) == 0x80) {
                            buffer[index++] = value;
                            state = State.POSSIBLE_UTF8_3BYTE;
                        }
                        else
                            delegateToWindows1252(value);
                    }
                    else
                        delegateToWindows1252(value);
                }
                else {
                    emit(first);
                    index = 0;
                    if (value >= 0x80) {
                        if (value >= 0xC0 && value <= 0xF7) {
                            buffer[index++] = value;
                            state = State.POSSIBLE_UTF8;
                        }
                        else
                            delegateToWindows1252(value);
                    }
                    else {
                        emit(value);
                        state = State.UNDETERMINED;
                    }
                }
            }
            break;
        case FIRST_TWO_ANY_00:
            if (value == 0) {
                buffer[index++] = 0;
                state = State.FIRST_THREE_ANY_00_00;
            }
            else {
                switchTo(new UTF16LE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
                delegate.accept(value);
            }
            break;
        case FIRST_THREE_ANY_00_00:
            switchTo(value == 0 ? new UTF32LE_CodePoint<>(getDownstream()) :
                    new UTF16LE_UTF16<>(new UTF16_CodePoint<>(getDownstream())));
            delegate.accept(value);
            break;
        case UNDETERMINED:
            if (value >= 0x80) {
                if (value >= 0xC0 && value <= 0xF7) {
                    buffer[index++] = value;
                    state = State.POSSIBLE_UTF8;
                }
                else
                    delegateToWindows1252(value);
            }
            else
                emit(value);
            break;
        case POSSIBLE_UTF8:
            if ((value & 0xC0) == 0x80) {
                if ((buffer[0] & 0x20) == 0)
                    delegateToUTF8(value);
                else {
                    buffer[index++] = value;
                    state = State.POSSIBLE_UTF8_3BYTE;
                }
            }
            else
                delegateToWindows1252(value);
            break;
        case POSSIBLE_UTF8_3BYTE:
            if ((value & 0xC0) == 0x80) {
                if ((buffer[0] & 0x10) == 0)
                    delegateToUTF8(value);
                else {
                    buffer[index++] = value;
                    state = State.POSSIBLE_UTF8_4BYTE;
                }
            }
            else
                delegateToWindows1252(value);
            break;
        case POSSIBLE_UTF8_4BYTE:
            if ((value & 0xC0) == 0x80)
                delegateToUTF8(value);
            else
                delegateToWindows1252(value);
            break;
        case DELEGATED:
            delegate.accept(value);
        }
    }

    /**
     * Close the pipeline.
     */
    @Override
    public void close() throws Exception {
        switch (state) {
            case INITIAL:
            case FIRST_TWO_FF_FE:
            case UNDETERMINED:
                break; // OK
            case FIRST_00:
            case FIRST_TWO_00_00:
            case FIRST_THREE_00_00_FE:
            case FIRST_THREE_FF_FE_00:
            case FIRST_TWO_ANY_00:
            case FIRST_THREE_ANY_00_00:
                // warning?
                break;
            case FIRST_FF:
            case FIRST_FE:
            case FIRST_EF:
            case FIRST_TWO_EF_BB:
            case FIRST_ANY:
            case POSSIBLE_UTF8:
            case POSSIBLE_UTF8_3BYTE:
            case POSSIBLE_UTF8_4BYTE:
                switchTo(new Windows1252_CodePoint<>(getDownstream()));
                break;
            case DELEGATED:
                delegate.close();
        }
        super.close();
    }

    /**
     * Return {@code true} if all sequences in the input to this stage of the pipeline are complete, that is, the input
     * is not in the middle of a sequence requiring more data.
     *
     * @return {@code true} if the input is in the "complete" state
     */
    @Override
    public boolean isStageComplete() {
        switch (state) {
            case INITIAL:
            case UNDETERMINED:
                return true;
            case DELEGATED:
                return delegate.isComplete();
            default:
                return false;
        }
    }

    /**
     * Switch to the specified delegate.
     *
     * @param   delegate    the new delegate
     * @throws  Exception   if thrown by a {@code close()} method
     */
    @Override
    public void switchTo(IntAcceptor<? extends R> delegate) throws Exception {
        this.delegate = delegate;
        state = State.DELEGATED;
        for (int i = 0; i < index; i++)
            delegate.accept(buffer[i]);
        index = 0;
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charset     the {@link Charset}
     * @throws  Exception   if thrown by a {@code close()} method
     */
    public void switchTo(Charset charset) throws Exception {
        switchTo(DecoderFactory.getDecoder(charset, getDownstream()));
    }

    /**
     * Switch to the nominated character set.
     *
     * @param   charsetName the character set name
     * @throws  Exception   if thrown by a {@code close()} method
     */
    public void switchTo(String charsetName) throws Exception {
        switchTo(DecoderFactory.getDecoder(charsetName, getDownstream()));
    }

    private void delegateToUTF8(int value) throws Exception {
        switchTo(new UTF8_CodePoint<>(getDownstream()));
        delegate.accept(value);
    }

    private void delegateToWindows1252(int value) throws Exception {
        switchTo(new Windows1252_CodePoint<>(getDownstream()));
        delegate.accept(value);
    }

}
