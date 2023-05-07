/*
 * @(#) Base64DecoderTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2023 Peter Wall
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

package net.pwall.pipeline.base64;

import net.pwall.pipeline.ByteArrayAcceptor;
import net.pwall.pipeline.StringAcceptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        byte[] result = pipeline.getResult();
        assertEquals(3, result.length);
        assertEquals((byte)0xFB, result[0]);
        assertEquals((byte)0xFF, result[1]);
        assertEquals((byte)0xBF, result[2]);
    }

    @Test
    public void shouldDecodeURLSpecialCharacters() {
        Base64Decoder<byte[]> pipeline = new Base64Decoder<>(new ByteArrayAcceptor());
        pipeline.accept("-_-_");
        byte[] result = pipeline.getResult();
        assertEquals(3, result.length);
        assertEquals((byte)0xFB, result[0]);
        assertEquals((byte)0xFF, result[1]);
        assertEquals((byte)0xBF, result[2]);
    }

}
