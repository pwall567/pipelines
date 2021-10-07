/*
 * @(#) AppendableAcceptor.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021 Peter Wall
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

package net.pwall.pipeline;

public class AppendableAcceptor<R> extends AbstractIntAcceptor<R> {

    private final Appendable appendable;

    public AppendableAcceptor(Appendable appendable) {
        this.appendable = appendable;
    }

    /**
     * Accept an {@code int}, after {@code closed} check and test for end of data.  Forward the value to the
     * {@link Appendable} as a {@code char}.
     *
     * @param   value       the input value
     * @throws  Exception   if thrown by the {@link Appendable} or by a {@code close()} method
     */
    @Override
    public void acceptInt(int value) throws Exception {
        appendable.append((char)value);
    }

}
