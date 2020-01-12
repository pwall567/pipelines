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

import java.util.List;

import net.pwall.util.pipeline.CodePoint_UTF8;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodePointUTF8Test {

    @Test
    public void shouldPassThroughASCII() throws Exception {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new TestIntAcceptor());
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
    }

    @Test
    public void shouldPassThroughMultipleASCII() throws Exception {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new TestIntAcceptor());
        pipe.accept('A');
        pipe.accept('B');
        pipe.accept('C');
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
        assertEquals(Integer.valueOf('B'), result.get(1));
        assertEquals(Integer.valueOf('C'), result.get(2));
    }

    @Test
    public void shouldPassThroughTwoByteChars() throws Exception {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new TestIntAcceptor());
        pipe.accept(0xA9);
        pipe.accept(0xF7);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(4, result.size());
        assertEquals(Integer.valueOf(0xC2), result.get(0));
        assertEquals(Integer.valueOf(0xA9), result.get(1));
        assertEquals(Integer.valueOf(0xC3), result.get(2));
        assertEquals(Integer.valueOf(0xB7), result.get(3));
    }

    @Test
    public void shouldPassThroughThreeByteChars() throws Exception {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new TestIntAcceptor());
        pipe.accept(0x2014);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0xE2), result.get(0));
        assertEquals(Integer.valueOf(0x80), result.get(1));
        assertEquals(Integer.valueOf(0x94), result.get(2));
    }

}
