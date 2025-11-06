/*
 * @(#) FullURIEncoderTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2022, 2023, 2025 Peter Wall
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

package io.jstuff.pipeline.uri;

import java.util.List;

import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.CodePoint_UTF16;
import io.jstuff.pipeline.codec.UTF16_CodePoint;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FullURIEncoderTest {

    @Test
    public void shouldEncodeSchemaFragmentWithSyntaxCharactersUnmodified() {
        IntPipeline<String> pipeline1 = new FullURIEncoder<>(new StringAcceptor());
        pipeline1.accept("https://example.com/");
        assertEquals("https://example.com/", pipeline1.getResult());
    }

    @Test
    public void shouldEncodeReservedCharacters() {
        IntPipeline<String> pipeline1 = new FullURIEncoder<>(new StringAcceptor());
        pipeline1.accept("https://example.com/?q=\"test data 100%\"");
        assertEquals("https://example.com/?q=%22test%20data%20100%25%22", pipeline1.getResult());
    }

    @Test
    public void shouldConvertStringUsingConvertFunction() {
        String input = "https://example.com/?q=\"test data 100%\"";
        assertEquals("https://example.com/?q=%22test%20data%20100%25%22", FullURIEncoder.convert(input));
    }

    @Test
    public void shouldConvertListUsingConvertFunction() {
        String input = "https://example.com/?q=\"test data 100%\"";
        List<Integer> inputList = UTF16_CodePoint.convert(input);
        List<Integer> outputList = FullURIEncoder.convert(inputList);
        assertEquals("https://example.com/?q=%22test%20data%20100%25%22", CodePoint_UTF16.convert(outputList));
    }

}
