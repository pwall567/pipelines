/*
 * @(#) URIEncoder.java
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
 * URI encoder - encode text using URI percent-encoding.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class URIEncoder<R> extends AbstractIntPipeline<R> {

    private static final char[] hexChars =
            new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private final boolean encodeSpaceAsPlus;

    public URIEncoder(boolean encodeSpaceAsPlus, IntAcceptor<? extends R> downstream) {
        super(downstream);
        this.encodeSpaceAsPlus = encodeSpaceAsPlus;
    }

    public URIEncoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
        this.encodeSpaceAsPlus = false;
    }

    @Override
    public void acceptInt(int value) throws Exception {
        if (value == ' ' && encodeSpaceAsPlus)
            emit('+');
        else if (!isUnreservedURI(value)) {
            emit('%');
            emit(hexChars[(value >> 4) & 0xF]);
            emit(hexChars[value & 0xF]);
        }
        else
            emit(value);
    }

    public static boolean isUnreservedURI(int ch) {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' ||
                ch == '-' || ch == '.' || ch == '_' || ch == '~';
    }

}
