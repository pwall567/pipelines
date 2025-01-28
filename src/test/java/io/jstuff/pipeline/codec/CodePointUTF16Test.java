/*
 * @(#) CodePointUTF16Test.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2023 Peter Wall
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

package io.jstuff.pipeline.codec;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.jstuff.pipeline.TestIntAcceptor;

public class CodePointUTF16Test {

    @Test
    public void shouldPassThroughBMPCodePoint() {
        CodePoint_UTF16<List<Integer>> pipe = new CodePoint_UTF16<>(new TestIntAcceptor());
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
    }

    @Test
    public void shouldExpandHighCodePointToSurrogates() {
        CodePoint_UTF16<List<Integer>> pipe = new CodePoint_UTF16<>(new TestIntAcceptor());
        pipe.accept(0x1F602);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0xD83D), result.get(0));
        assertEquals(Integer.valueOf(0xDE02), result.get(1));
    }

}
