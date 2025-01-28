/*
 * @(#) XMLDecoder.java
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

package io.jstuff.pipeline.xml;

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.xxml.MappingEntry;
import io.jstuff.pipeline.xxml.DecoderBase;

/**
 * XML decoder - decode text encoded with XML escaping.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class XMLDecoder<R> extends DecoderBase<R> {

    public static final MappingEntry[] table = new MappingEntry[] {
            new MappingEntry('&', "amp"),
            new MappingEntry('\'', "apos"),
            new MappingEntry('>', "gt"),
            new MappingEntry('<', "lt"),
            new MappingEntry('"', "quot")
    };

    public XMLDecoder(IntAcceptor<? extends R> downstream) {
        super(table, downstream);
    }

}
