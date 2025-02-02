/*
 * @(#) ByteArrayPipelineTest.java
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

package io.jstuff.pipeline.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.StringAcceptor;

public class ByteArrayPipelineTest {

    @Test
    public void shouldProcessByteArray() {
        String str = "The quick brown etc.";
        int n = str.length();
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++)
            bytes[i] = (byte)str.charAt(i);
        ByteArrayPipeline<String> bap = new ByteArrayPipeline<>(new StringAcceptor());
        bap.accept(bytes);
        assertEquals(str, bap.getResult());
    }

    @Test
    public void shouldProcessByteArrayUsingOffsetAndLength() {
        String str = "The quick brown etc.";
        int n = str.length();
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++)
            bytes[i] = (byte)str.charAt(i);
        ByteArrayPipeline<String> bap = new ByteArrayPipeline<>(new StringAcceptor());
        bap.accept(bytes, 2, 10);
        assertEquals(str.substring(2, 12), bap.getResult());
    }

}
