/*
 * @(#) CodePointUTF8Test.java
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

package net.pwall.util.pipeline.test;

import net.pwall.util.pipeline.CodePoint_UTF8;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodePointUTF8Test {

    @Test
    public void shouldPassThroughASCII() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        CodePoint_UTF8 pipe = new CodePoint_UTF8(testIntPipeline);
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        assertEquals(1, testIntPipeline.size());
        assertEquals('A', testIntPipeline.get(0));
    }

    @Test
    public void shouldPassThroughMultipleASCII() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        CodePoint_UTF8 pipe = new CodePoint_UTF8(testIntPipeline);
        pipe.accept('A');
        pipe.accept('B');
        pipe.accept('C');
        assertTrue(pipe.isComplete());
        assertEquals(3, testIntPipeline.size());
        assertEquals('A', testIntPipeline.get(0));
        assertEquals('B', testIntPipeline.get(1));
        assertEquals('C', testIntPipeline.get(2));
    }

    @Test
    public void shouldPassThroughTwoByteChars() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        CodePoint_UTF8 pipe = new CodePoint_UTF8(testIntPipeline);
        pipe.accept(0xA9);
        pipe.accept(0xF7);
        assertTrue(pipe.isComplete());
        assertEquals(4, testIntPipeline.size());
        assertEquals(0xC2, testIntPipeline.get(0));
        assertEquals(0xA9, testIntPipeline.get(1));
        assertEquals(0xC3, testIntPipeline.get(2));
        assertEquals(0xB7, testIntPipeline.get(3));
    }

    @Test
    public void shouldPassThroughThreeByteChars() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        CodePoint_UTF8 pipe = new CodePoint_UTF8(testIntPipeline);
        pipe.accept(0x2014);
        assertTrue(pipe.isComplete());
        assertEquals(3, testIntPipeline.size());
        assertEquals(0xE2, testIntPipeline.get(0));
        assertEquals(0x80, testIntPipeline.get(1));
        assertEquals(0x94, testIntPipeline.get(2));
    }

}
