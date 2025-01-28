/*
 * @(#) HTMLDecoder.java
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

package io.jstuff.pipeline.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.jstuff.pipeline.IntAcceptor;
import io.jstuff.pipeline.xxml.MappingEntry;
import io.jstuff.pipeline.xxml.DecoderBase;

/**
 * HTML decoder - decode text encoded with HTML escaping.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class HTMLDecoder<R> extends DecoderBase<R> {

    public static final MappingEntry[] reverseTable;

    static {
        int baseEntitiesLength = HTMLEncoder.baseEntities.length;
        int mappedEntitiesLength = HTMLEncoder.mappedEntities.length;
        List<MappingEntry> list = new ArrayList<>(baseEntitiesLength + mappedEntitiesLength + 4);
        list.add(new MappingEntry('"', "quot"));
        list.add(new MappingEntry('&', "amp"));
        list.add(new MappingEntry('<', "lt"));
        list.add(new MappingEntry('>', "gt"));
        for (int i = 0; i < baseEntitiesLength; i++)
            list.add(new MappingEntry(i + 0xA0, HTMLEncoder.baseEntities[i]));
        list.addAll(Arrays.asList(HTMLEncoder.mappedEntities));
        reverseTable = list.toArray(new MappingEntry[0]);
        Arrays.sort(reverseTable);
    }

    public HTMLDecoder(IntAcceptor<? extends R> downstream) {
        super(reverseTable, downstream);
    }

}
