/*
 * @(#) PipelinesTest.java
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

package io.jstuff.pipeline;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PipelinesTest {

    @Test
    public void shouldOutputInteger() {
        StringAcceptor stringAcceptor = new StringAcceptor();
        Pipelines.acceptInt(stringAcceptor, 123456);
        assertEquals("123456", stringAcceptor.getResult());
        stringAcceptor.clear();
        Pipelines.acceptInt(stringAcceptor, -22334455);
        assertEquals("-22334455", stringAcceptor.getResult());
    }

}
