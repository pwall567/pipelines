/*
 * @(#) CodePoint_UTF8.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020 Peter Wall
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

package net.pwall.util.pipeline;

import java.util.function.IntConsumer;

/**
 * An {@link IntPipeline} to convert Unicode copepoints to UTF-8.
 *
 * @author  Peter Wall
 */
public class CodePoint_UTF8 extends AbstractIntPipeline {

    public CodePoint_UTF8(IntConsumer byteConsumer) {
        super(byteConsumer);
    }

    @Override
    public void internalAccept(int value) {
        if (value <= 0x7F)
            forward(value);
        else if (value <= 0x7FF) {
            forward(0xC0 | value >> 6);
            forward(0x80 | (value & 0x3F));
        }
        else if (value <= 0xFFFF) {
            forward(0xE0 | value >> 12);
            forward(0x80 | ((value >> 6) & 0x3F));
            forward(0x80 | (value & 0x3F));
        }
        else if (value <= 0x10FFFF) {
            forward(0xF0 | value >> 18);
            forward(0x80 | ((value >> 12) & 0x3F));
            forward(0x80 | ((value >> 6) & 0x3F));
            forward(0x80 | (value & 0x3F));
        }
        else
            throw new IllegalArgumentException("Illegal codepoint");
    }

}
