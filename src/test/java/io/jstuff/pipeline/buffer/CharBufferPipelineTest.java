/*
 * @(#) CharBufferPipelineTest.java
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

import java.nio.CharBuffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.StringAcceptor;

public class CharBufferPipelineTest {

    @Test
    public void shouldProcessByteBuffer() {
        String str = "'Tis all a Chequer-board of Nights and Days";
        int n = str.length();
        CharBuffer charBuffer = CharBuffer.allocate(n);
        for (int i = 0; i < n; i++)
            charBuffer.put(str.charAt(i));
        charBuffer.flip();
        CharBufferPipeline<String> cbp = new CharBufferPipeline<>(new StringAcceptor());
        cbp.accept(charBuffer);
        assertEquals(str, cbp.getResult());
    }

}
