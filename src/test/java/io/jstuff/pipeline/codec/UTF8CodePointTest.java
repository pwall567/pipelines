/*
 * @(#) UTF8CodePointTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2021, 2023 Peter Wall
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

import java.nio.ByteBuffer;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import io.jstuff.pipeline.TestIntAcceptor;

public class UTF8CodePointTest {

    @Test
    public void shouldPassThroughSingleChar() {
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept('A');
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
    }

    @Test
    public void shouldPassThroughSingleCharPlusTerminator() {
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept('A');
        pipe.accept(-1);
        assertTrue(pipe.isComplete());
        assertTrue(pipe.isClosed());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
    }

    @Test
    public void shouldPassThroughTwoByteChars() {
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept(0xC2);
        assertFalse(pipe.isComplete());
        pipe.accept(0xA9);
        pipe.accept(0xC3);
        pipe.accept(0xB7);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0xA9), result.get(0));
        assertEquals(Integer.valueOf(0xF7), result.get(1));
    }

    @Test
    public void shouldPassThroughThreeByteChars() {
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept(0xE2);
        pipe.accept(0x80);
        pipe.accept(0x94);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(0x2014), result.get(0));
    }

    @Test
    public void shouldFailOnGetResultWhenPipelineNotComplete() {
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept(0xE2);
        assertFalse(pipe.isComplete());
        IllegalStateException e = assertThrows(IllegalStateException.class, pipe::getResult);
        assertEquals("Sequence is not complete", e.getMessage());
    }

    @Test
    public void shouldReadByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        byteBuffer.put((byte)'A');
        byteBuffer.put((byte)' ');
        byteBuffer.put((byte)0xE2);
        byteBuffer.put((byte)0x80);
        byteBuffer.put((byte)0x94);
        byteBuffer.put((byte)' ');
        byteBuffer.put((byte)'B');
        byteBuffer.flip();
        UTF8_CodePoint<List<Integer>> pipe = new UTF8_CodePoint<>(new TestIntAcceptor());
        pipe.accept(byteBuffer);
        assertTrue(pipe.isComplete());
        List<Integer> result = pipe.getResult();
        assertEquals(5, result.size());
        assertEquals(Integer.valueOf('A'), result.get(0));
        assertEquals(Integer.valueOf(' '), result.get(1));
        assertEquals(Integer.valueOf(0x2014), result.get(2));
        assertEquals(Integer.valueOf(' '), result.get(3));
        assertEquals(Integer.valueOf('B'), result.get(4));
    }

}
