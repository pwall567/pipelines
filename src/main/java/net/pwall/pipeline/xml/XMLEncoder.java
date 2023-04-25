/*
 * @(#) XMLEncoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2022, 2023 Peter Wall
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

package net.pwall.pipeline.xml;

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.codec.EncoderBase;

/**
 * XML encoder - encode text using XML escaping.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class XMLEncoder<R> extends EncoderBase<R> {

    public XMLEncoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    @Override
    public void acceptInt(int value) {
        if (value == '"')
            emit("&quot;");
        else if (value == '&')
            emit("&amp;");
        else if (value == '\'')
            emit("&apos;");
        else if (value == '<')
            emit("&lt;");
        else if (value == '>')
            emit("&gt;");
        else if (value >= ' ' && value < 0x7F)
            emit(value);
        else {
            emit("&#x");
            emitHex(value);
            emit(';');
        }
    }

}
