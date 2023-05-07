/*
 * @(#) Base64EncoderTest.java
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

import net.pwall.pipeline.StringAcceptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base64EncoderTest {

    @Test
    public void shouldEncodeSimpleString() throws Exception {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor());
        pipeline.accept("ABCD");
        pipeline.close();
        assertEquals("QUJDRA==", pipeline.getResult());
    }

    @Test
    public void shouldEncodeHighCharacters() throws Exception {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor());
        pipeline.accept(0xFB);
        pipeline.accept(0xFF);
        pipeline.accept(0xBF);
        pipeline.close();
        assertEquals("+/+/", pipeline.getResult());
    }

    @Test
    public void shouldEncodeSimpleStringUsingURLEncoding() throws Exception {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor(), true);
        pipeline.accept("ABCD");
        pipeline.close();
        assertEquals("QUJDRA", pipeline.getResult());
    }

    @Test
    public void shouldEncodeHighCharactersUsingURLEncoding() throws Exception {
        Base64Encoder<String> pipeline = new Base64Encoder<>(new StringAcceptor(), true);
        pipeline.accept(0xFB);
        pipeline.accept(0xFF);
        pipeline.accept(0xBF);
        pipeline.close();
        assertEquals("-_-_", pipeline.getResult());
    }

}
