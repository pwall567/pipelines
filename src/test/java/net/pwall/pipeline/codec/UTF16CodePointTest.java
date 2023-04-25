/*
 * @(#) UTF16CodePointTest.java
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

package net.pwall.pipeline.codec;

import java.util.List;

import net.pwall.pipeline.TestIntAcceptor;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UTF16CodePointTest {

    @Test
    public void shouldPassThroughBMPCodePoint() {
        TestIntAcceptor testIntPipeline = new TestIntAcceptor();
        UTF16_CodePoint<List<Integer>> pipe = new UTF16_CodePoint<>(testIntPipeline);
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
    }

    @Test
    public void shouldConvertSurrogatePair() {
        TestIntAcceptor testIntPipeline = new TestIntAcceptor();
        UTF16_CodePoint<List<Integer>> pipe = new UTF16_CodePoint<>(testIntPipeline);
        pipe.accept(0xD83D);
        assertFalse(pipe.isComplete());
        pipe.accept(0xDE02);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(0x1F602), result.get(0));
    }

}
