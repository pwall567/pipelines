/*
 * @(#) Base64EncoderTest.java
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

package io.jstuff.pipeline.base64;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.CodePoint_UTF16;
import io.jstuff.pipeline.codec.UTF16_CodePoint;

public class Base64EncoderTest {

    @Test
    public void shouldEncodeSimpleString() {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor());
        pipeline.accept("ABCD");
        pipeline.safeClose();
        assertEquals("QUJDRA==", pipeline.getResult());
    }

    @Test
    public void shouldEncodeHighCharacters() {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor());
        pipeline.accept(0xFB);
        pipeline.accept(0xFF);
        pipeline.accept(0xBF);
        pipeline.safeClose();
        assertEquals("+/+/", pipeline.getResult());
    }

    @Test
    public void shouldEncodeSimpleStringUsingURLEncoding() {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor(), true);
        pipeline.accept("ABCD");
        pipeline.safeClose();
        assertEquals("QUJDRA", pipeline.getResult());
    }

    @Test
    public void shouldEncodeHighCharactersUsingURLEncoding() {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor(), true);
        pipeline.accept(0xFB, 0xFF, 0xBF);
        pipeline.safeClose();
        assertEquals("-_-_", pipeline.getResult());
    }

    @Test
    public void shouldConvertByteArrayUsingConvertFunction() {
        byte[] input = new byte[] { 'A', 'B', 'C', 'D' };
        assertArrayEquals(new byte[] { 'Q', 'U', 'J', 'D', 'R', 'A', '=', '=' }, Base64Encoder.convert(input));
    }

    @Test
    public void shouldConvertStringUsingConvertFunction() {
        String input = "ABCD";
        assertEquals("QUJDRA==", Base64Encoder.convert(input));
    }

    @Test
    public void shouldConvertListUsingConvertFunction() {
        String input = "ABCD";
        List<Integer> inputList = UTF16_CodePoint.convert(input);
        List<Integer> outputList = Base64Encoder.convert(inputList);
        assertEquals("QUJDRA==", CodePoint_UTF16.convert(outputList));
    }

}
