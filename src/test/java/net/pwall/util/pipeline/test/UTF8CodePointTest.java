/*
 * @(#) UTF8CodePointTest.java
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

import net.pwall.util.pipeline.UTF8_CodePoint;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UTF8CodePointTest {

    @Test
    public void shouldPassThroughSingleChar() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        UTF8_CodePoint pipe = new UTF8_CodePoint(testIntPipeline);
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        assertEquals(1, testIntPipeline.size());
        assertEquals('A', testIntPipeline.get(0));
    }

    @Test
    public void shouldPassThroughSingleCharPlusTerminator() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        UTF8_CodePoint pipe = new UTF8_CodePoint(testIntPipeline);
        pipe.accept('A');
        pipe.accept(-1);
        assertTrue(pipe.isComplete());
        assertTrue(pipe.isClosed());
        assertEquals(1, testIntPipeline.size());
        assertEquals('A', testIntPipeline.get(0));
    }

    @Test
    public void shouldPassThroughTwoByteChars() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        UTF8_CodePoint pipe = new UTF8_CodePoint(testIntPipeline);
        pipe.accept(0xC2);
        assertFalse(pipe.isComplete());
        pipe.accept(0xA9);
        pipe.accept(0xC3);
        pipe.accept(0xB7);
        assertTrue(pipe.isComplete());
        assertEquals(2, testIntPipeline.size());
        assertEquals(0xA9, testIntPipeline.get(0));
        assertEquals(0xF7, testIntPipeline.get(1));
    }

    @Test
    public void shouldPassThroughThreeByteChars() {
        TestIntPipeline testIntPipeline = new TestIntPipeline();
        UTF8_CodePoint pipe = new UTF8_CodePoint(testIntPipeline);
        pipe.accept(0xE2);
        pipe.accept(0x80);
        pipe.accept(0x94);
        assertTrue(pipe.isComplete());
        assertEquals(1, testIntPipeline.size());
        assertEquals(0x2014, testIntPipeline.get(0));
    }

}
