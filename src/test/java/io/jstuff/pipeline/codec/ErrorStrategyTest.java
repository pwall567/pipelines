/*
 * @(#) ErrorStrategyTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2023, 2025 Peter Wall
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
import static org.junit.Assert.assertThrows;

import io.jstuff.pipeline.ListIntAcceptor;

public class ErrorStrategyTest {

    @Test
    public void shouldThrowExceptionAsDefaultErrorStrategy() {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new ListIntAcceptor());
        EncoderException e = assertThrows(EncoderException.class, () -> pipe.accept(0x11ABCD));
        assertEquals("Illegal value 0x11ABCD", e.getMessage());
        assertEquals(0x11ABCD, e.getErrorValue());
    }

    @Test
    public void shouldIgnoreErrorCharactersWhenIgnoreSelected() {
        CodePoint_UTF8<List<Integer>> pipe = new CodePoint_UTF8<>(new ListIntAcceptor(), ErrorStrategy.IGNORE);
        pipe.accept(0x4F);
        pipe.accept(0x11ABCD);
        pipe.accept(0x4B);
        List<Integer> result = pipe.getResult();
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0x4F), result.get(0));
        assertEquals(Integer.valueOf(0x4B), result.get(1));
    }

    @Test
    public void shouldSubstituteDefaultCharacterWhenSelected() {
        CodePoint_UTF8<List<Integer>> pipe =
                new CodePoint_UTF8<>(new ListIntAcceptor(), new ErrorStrategy.Substitute());
        pipe.accept(0x4F);
        pipe.accept(0x11ABCD);
        pipe.accept(0x4B);
        List<Integer> result = pipe.getResult();
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0x4F), result.get(0));
        assertEquals(Integer.valueOf(0xBF), result.get(1));
        assertEquals(Integer.valueOf(0x4B), result.get(2));
    }

    @Test
    public void shouldSubstituteNominatedCharacterWhenSelected() {
        CodePoint_UTF8<List<Integer>> pipe =
                new CodePoint_UTF8<>(new ListIntAcceptor(), new ErrorStrategy.Substitute(0xA5));
        pipe.accept(0x4F);
        pipe.accept(0x11ABCD);
        pipe.accept(0x4B);
        List<Integer> result = pipe.getResult();
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0x4F), result.get(0));
        assertEquals(Integer.valueOf(0xA5), result.get(1));
        assertEquals(Integer.valueOf(0x4B), result.get(2));
    }

}
