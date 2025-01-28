/*
 * @(#) DynamicDecoderTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2023 Peter Wall
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.buffer.CharSequencePipeline;

public class DynamicDecoderTest {

    private static final String testString1 = "Mary had a little lamb";
    private static final String utf8BOM = "\u00EF\u00BB\u00BF";
    private static final String utf8EuroSign = "Euros \u00E2\u0082\u00AC200.00";
    private static final String windows1252EuroSign = "Euros \u0080200.00";
    private static final String resume = "R\u00E9sum\u00E9";

    @Test
    public void shouldAcceptUnlabelledStreamOfBytes() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        CharSequencePipeline<String> pipeline = new CharSequencePipeline<>(dd);
        pipeline.accept(testString1);
        assertEquals(testString1, pipeline.getResult());
        assertEquals("UNDETERMINED", dd.state.name());
    }

    @Test
    public void shouldAcceptStringWithUTF8ByteOrderMark() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        CharSequencePipeline<String> pipeline = new CharSequencePipeline<>(dd);
        pipeline.accept(utf8BOM, testString1);
        assertEquals(testString1, pipeline.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF8_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseUTF8InUnlabelledString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        CharSequencePipeline<String> pipeline = new CharSequencePipeline<>(dd);
        pipeline.accept(utf8EuroSign);
        assertEquals("Euros \u20AC200.00", pipeline.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF8_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseWindows1252InUnlabelledString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        CharSequencePipeline<String> pipeline = new CharSequencePipeline<>(dd);
        pipeline.accept(windows1252EuroSign);
        assertEquals("Euros \u20AC200.00", pipeline.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.Windows1252_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseWindows1252InAnotherUnlabelledString() throws Exception {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        CharSequencePipeline<String> pipeline = new CharSequencePipeline<>(dd);
        pipeline.accept(resume);
        pipeline.close();
        assertEquals(resume, pipeline.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.Windows1252_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseUTF16LEString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        for (int i = 0, n = testString1.length(); i < n; i++)
            dd.accept(testString1.charAt(i), 0);
        assertEquals(testString1, dd.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF16LE_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseUTF16BEString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        for (int i = 0, n = testString1.length(); i < n; i++)
            dd.accept(0, testString1.charAt(i));
        assertEquals(testString1, dd.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF16BE_UTF16", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseUTF32LEString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        for (int i = 0, n = testString1.length(); i < n; i++)
            dd.accept(testString1.charAt(i), 0, 0, 0);
        assertEquals(testString1, dd.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF32LE_CodePoint", dd.delegate.getClass().getName());
    }

    @Test
    public void shouldRecogniseUTF32BEString() {
        DynamicDecoder<String> dd = new DynamicDecoder<>(new StringAcceptor());
        for (int i = 0, n = testString1.length(); i < n; i++)
            dd.accept(0, 0, 0, testString1.charAt(i));
        assertEquals(testString1, dd.getResult());
        assertEquals("DELEGATED", dd.state.name());
        assertEquals("io.jstuff.pipeline.codec.UTF32BE_CodePoint", dd.delegate.getClass().getName());
    }

}
