/*
 * @(#) MappingEntry.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2022 Peter Wall
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

/**
 * Mapping entry for XML and HTML entity mapping.
 *
 * @author  Peter Wall
 */
public class MappingEntry implements Comparable<MappingEntry> {

    private final int codePoint;
    private final String string;

    public MappingEntry(int codePoint, String string) {
        this.codePoint = codePoint;
        this.string = string;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public String getString() {
        return string;
    }

    @Override
    public int compareTo(MappingEntry o) {
        return string.compareTo(o.string);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MappingEntry))
            return false;
        MappingEntry other = (MappingEntry)obj;
        return codePoint == other.codePoint && string.equals(other.string);
    }

    @Override
    public int hashCode() {
        return codePoint ^ string.hashCode();
    }

}
