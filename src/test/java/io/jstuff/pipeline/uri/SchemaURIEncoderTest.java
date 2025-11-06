/*
 * @(#) SchemaURIEncoderTest.java
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.StringAcceptor;
import io.jstuff.pipeline.codec.CodePoint_UTF16;
import io.jstuff.pipeline.codec.UTF16_CodePoint;

public class SchemaURIEncoderTest {

    @Test
    public void shouldEncodeSchemaFragmentWithDollarSignUnmodified() {
        IntPipeline<String> pipeline1 = new SchemaURIEncoder<>(new StringAcceptor());
        pipeline1.accept("$ref");
        assertEquals("$ref", pipeline1.getResult());
    }

    @Test
    public void shouldEncodeReservedCharacters() {
        IntPipeline<String> pipeline1 = new SchemaURIEncoder<>(new StringAcceptor());
        pipeline1.accept("Hello, World!");
        assertEquals("Hello%2C%20World%21", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new SchemaURIEncoder<>(new StringAcceptor());
        pipeline2.accept("a more-complicated string: $a/b+c%e.(???)");
        assertEquals("a%20more-complicated%20string%3A%20$a%2Fb%2Bc%25e.%28%3F%3F%3F%29", pipeline2.getResult());
    }

    @Test
    public void shouldConvertStringUsingConvertFunction() {
        String input = "a more-complicated string: $a/b+c%e.(???)";
        assertEquals("a%20more-complicated%20string%3A%20$a%2Fb%2Bc%25e.%28%3F%3F%3F%29",
                SchemaURIEncoder.convert(input));
    }

    @Test
    public void shouldConvertListUsingConvertFunction() {
        String input = "a more-complicated string: $a/b+c%e.(???)";
        List<Integer> inputList = UTF16_CodePoint.convert(input);
        List<Integer> outputList = SchemaURIEncoder.convert(inputList);
        assertEquals("a%20more-complicated%20string%3A%20$a%2Fb%2Bc%25e.%28%3F%3F%3F%29",
                CodePoint_UTF16.convert(outputList));
    }

}
