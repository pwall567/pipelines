/*
 * @(#) URIDecoder.java
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

package net.pwall.pipeline.uri;

import net.pwall.pipeline.AbstractIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * URI decoder - decode text using URI percent-encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class URIDecoder<R> extends AbstractIntPipeline<R> {

    enum State { NORMAL, FIRST, SECOND }

    private State state;
    private int character;

    public URIDecoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
        state = State.NORMAL;
    }

    @Override
    public void acceptInt(int value) throws Exception {
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

}
