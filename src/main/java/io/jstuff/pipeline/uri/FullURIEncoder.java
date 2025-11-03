/*
 * @(#) FullURIEncoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2022, 2023, 2025 Peter Wall
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

import static io.jstuff.util.IntOutput.output2Hex;

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.codec.EncoderBase;
import static io.jstuff.pipeline.uri.URIEncoder.isUnreservedURI;

/**
 * URI encoder for use with full URI &ndash; encode text using URI percent-encoding, not including characters used in
 * the URI syntax (<i>e.g.</i> {@code :}, {@code /} <i>etc.</i>).
 * <br>
 * This class provides functionality equivalent to the JavaScript {@code encodeURI()} function, while the
 * {@link URIEncoder} class is more strict and should be used on individual components of the URI.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class FullURIEncoder<R> extends EncoderBase<R> {

    private final boolean encodeSpaceAsPlus;

    public FullURIEncoder(IntAcceptor<? extends R> downstream, boolean encodeSpaceAsPlus) {
        super(downstream);
        this.encodeSpaceAsPlus = encodeSpaceAsPlus;
    }

    public FullURIEncoder(IntAcceptor<? extends R> downstream) {
        this(downstream, false);
    }

    @Override
    public void acceptInt(int value) {
        if (value == ' ' && encodeSpaceAsPlus)
            emit('+');
        else if (!(isUnreservedURI(value) || isSyntaxURI(value))) {
            emit('%');
            output2Hex(value, this::emit);
        }
        else
            emit(value);
    }

    public static boolean isSyntaxURI(int ch) {
        return ch == '!' || ch == '#' || ch == '$' || ch >= '&' && ch <= '/' || ch == ':' || ch == ';' || ch == '=' ||
                ch == '?' || ch == '@';
    }

}
