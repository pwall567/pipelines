/*
 * @(#) Base64DecoderTest.java
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
import static org.junit.Assert.assertThrows;

import io.jstuff.pipeline.ByteArrayAcceptor;
import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.CodePoint_UTF16;
import io.jstuff.pipeline.codec.EncoderException;
import io.jstuff.pipeline.codec.UTF16_CodePoint;

public class Base64DecoderTest {

    @Test
    public void shouldDecodeSimpleString() {
        Base64Decoder<String> pipeline = new Base64Decoder<>(new StringAcceptor());
        pipeline.accept("QUJDRA==");
        assertEquals("ABCD", pipeline.getResult());
    }

    @Test
    public void shouldDecodeSpecialCharacters() {
        Base64Decoder<byte[]> pipeline = new Base64Decoder<>(new ByteArrayAcceptor());
        pipeline.accept("+/+/");
        assertArrayEquals(new byte[] { (byte)0xFB, (byte)0xFF, (byte)0xBF }, pipeline.getResult());
    }

    @Test
    public void shouldDecodeURLSpecialCharacters() {
        Base64Decoder<byte[]> pipeline = new Base64Decoder<>(new ByteArrayAcceptor());
        pipeline.accept("-_-_");
        assertArrayEquals(new byte[] { (byte)0xFB, (byte)0xFF, (byte)0xBF }, pipeline.getResult());
    }

    @Test
    public void shouldThrowExceptionOnInvalidCharacter() {
        Base64Decoder<byte[]> pipeline = new Base64Decoder<>(new ByteArrayAcceptor());
        EncoderException e = assertThrows(EncoderException.class, () -> pipeline.accept('*'));
        assertEquals("Illegal value 0x2A", e.getMessage());
        assertEquals(0x2A, e.getErrorValue());
    }

    @Test
    public void shouldConvertByteArrayUsingConvertFunction() {
        byte[] input = new byte[] { 'Q', 'U', 'J', 'D', 'R', 'A', '=', '=' };
        assertArrayEquals(new byte[] { 'A', 'B', 'C', 'D' }, Base64Decoder.convert(input));
    }

    @Test
    public void shouldConvertStringUsingConvertFunction() {
        String input = "QUJDRA==";
        assertEquals("ABCD", Base64Decoder.convert(input));
    }

    @Test
    public void shouldConvertListUsingConvertFunction() {
        String input = "QUJDRA==";
        List<Integer> inputList = UTF16_CodePoint.convert(input);
        List<Integer> outputList = Base64Decoder.convert(inputList);
        assertEquals("ABCD", CodePoint_UTF16.convert(outputList));
    }

}
