/*
 * @(#) SchemaURIEncoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2022, 2023 Peter Wall
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

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.codec.EncoderBase;
import static net.pwall.pipeline.uri.URIEncoder.isUnreservedURI;
import static net.pwall.util.IntOutput.output2Hex;

/**
 * URI encoder for use with JSON Schema URI fragment - encode text using URI percent-encoding, leaving dollar sign
 * unencoded.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class SchemaURIEncoder<R> extends EncoderBase<R> {

    private final boolean encodeSpaceAsPlus;

    public SchemaURIEncoder(IntAcceptor<? extends R> downstream, boolean encodeSpaceAsPlus) {
        super(downstream);
        this.encodeSpaceAsPlus = encodeSpaceAsPlus;
    }

    public SchemaURIEncoder(IntAcceptor<? extends R> downstream) {
        this(downstream, false);
    }

    @Override
    public void acceptInt(int value) {
        if (value == ' ' && encodeSpaceAsPlus)
            emit('+');
        else if (!(isUnreservedURI(value) || value == '$')) {
            emit('%');
            output2Hex(value, this::emit);
        }
        else
            emit(value);
    }

}
