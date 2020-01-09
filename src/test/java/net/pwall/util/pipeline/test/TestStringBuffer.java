/*
 * @(#) TestStringBuffer.java
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

import net.pwall.util.pipeline.IntPipeline;

public class TestStringBuffer implements IntPipeline {

    private StringBuilder sb;
    private boolean closed;

    public TestStringBuffer(int initialSize) {
        sb = new StringBuilder(initialSize);
        closed = false;
    }

    public String getString() {
        return sb.toString();
    }

    @Override
    public void accept(int value) {
        if (isClosed())
            throw new IllegalStateException("Pipeline is closed");
        if (value == IntPipeline.END_OF_DATA) {
            closed = true;
        }
        else
            sb.append((char)value);
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

}
