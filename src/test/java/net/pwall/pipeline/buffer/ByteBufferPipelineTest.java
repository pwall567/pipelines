/*
 * @(#) ByteBufferPipelineTest.java
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

package net.pwall.pipeline.buffer;

import java.nio.ByteBuffer;

import net.pwall.pipeline.StringAcceptor;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ByteBufferPipelineTest {

    @Test
    public void shouldProcessByteBuffer() {
        String str = "Awake! for Morning in the Bowl of Night";
        int n = str.length();
        ByteBuffer byteBuffer = ByteBuffer.allocate(n);
        for (int i = 0; i < n; i++)
            byteBuffer.put((byte)str.charAt(i));
        byteBuffer.flip();
        ByteBufferPipeline<String> bbp = new ByteBufferPipeline<>(new StringAcceptor());
        bbp.accept(byteBuffer);
        assertEquals(str, bbp.getResult());
    }

}
