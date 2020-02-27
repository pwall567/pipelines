/*
 * @(#) TestReadingInputStream.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020 Peter Wall
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

package net.pwall.util.pipeline.test;

import java.io.InputStream;

import net.pwall.util.pipeline.CodePoint_UTF16;
import net.pwall.util.pipeline.IntPipeline;
import net.pwall.util.pipeline.StringAcceptor;
import net.pwall.util.pipeline.UTF8_CodePoint;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestReadingInputStream {

    @Test
    public void shouldReadAndConvertExternalResource() throws Exception {
        IntPipeline<String> pipe = new UTF8_CodePoint<>(new CodePoint_UTF16<>(new StringAcceptor(120)));
        InputStream inputStream = TestReadingInputStream.class.getResourceAsStream("/test1.txt");
        while (!pipe.isClosed())
            pipe.accept(inputStream.read());
        assertEquals("The quick brown fox jumps over the lazy dog.\n\n" +
                "And now for something completely different: \u2014 \u201C \u00C0 \u00C9 \u0130 \u00D4 \u00DC \u201D\n",
                pipe.getResult());
    }

    @Test
    public void shouldReadAndConvertExternalResourceUsingAcceptInputStream() throws Exception {
        IntPipeline<String> pipe = new UTF8_CodePoint<>(new CodePoint_UTF16<>(new StringAcceptor(120)));
        pipe.accept(TestReadingInputStream.class.getResourceAsStream("/test1.txt"));
        assertEquals("The quick brown fox jumps over the lazy dog.\n\n" +
                "And now for something completely different: \u2014 \u201C \u00C0 \u00C9 \u0130 \u00D4 \u00DC \u201D\n",
                pipe.getResult());
    }

}
