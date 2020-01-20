/*
 * @(#) UTF16_CodePoint.java
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

package net.pwall.util.pipeline;

/**
 * An {@link IntPipeline} to convert UTF-16 to Unicode code points.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class UTF16_CodePoint<R> extends AbstractIntPipeline<R> {

    /**
     * Local version of {@code IntConsumer} interface that allows exceptions on the {@code accept} method.
     */
    private interface IntConsumer {
        void accept(int value) throws Exception;
    }

    private IntConsumer state;
    private int highSurrogate;

    private final IntConsumer surrogate = this::terminal;
    private final IntConsumer normal = i -> {
        if (Character.isHighSurrogate((char)i)) {
            highSurrogate = i;
            state = surrogate;
        }
        else
            emit(i);
    };

    public UTF16_CodePoint(IntAcceptor<? extends R> downstream) {
        super(downstream);
        state = normal;
    }

    @Override
    public void acceptInt(int value) throws Exception {
        state.accept(value);
    }

    @Override
    public boolean isComplete() {
        return state == normal && super.isComplete();
    }

    private void terminal(int i) throws Exception {
        if (!Character.isLowSurrogate((char)i))
            throw new IllegalArgumentException("Illegal character in surrogate sequence");
        emit(Character.toCodePoint((char)highSurrogate, (char)i));
        state = normal;
    }

}
