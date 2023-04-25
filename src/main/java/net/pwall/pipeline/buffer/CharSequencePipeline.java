/*
 * @(#) CharSequencePipeline.java
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

import net.pwall.pipeline.AbstractObjectIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * A pipeline that takes {@link CharSequence} objects (e.g. {@link String}s) and emits each character as an {@code int}.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public class CharSequencePipeline<R> extends AbstractObjectIntPipeline<CharSequence, R> {

    /**
     * Construct a {@code CharSequencePipeline} with the given downstream {@link IntAcceptor}.
     *
     * @param   downstream  the {@link IntAcceptor}
     */
    public CharSequencePipeline(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    /**
     * Accept a {@link CharSequence}, after {@code closed} check and test for end of data.  Emit each character to the
     * downstream {@link IntAcceptor}.
     *
     * @param   value       the input value
     */
    @Override
    public void acceptObject(CharSequence value) {
        for (int i = 0, n = value.length(); i < n; i++)
            emit(value.charAt(i));
    }

}
