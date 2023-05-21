/*
 * @(#) UTF16_CodePoint.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2020, 2021, 2023 Peter Wall
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

package net.pwall.pipeline.codec;

import java.util.function.IntConsumer;

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.IntPipeline;

/**
 * A decoder {@link IntPipeline} to convert UTF-16 to Unicode code points.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class UTF16_CodePoint<R> extends ErrorStrategyBase<R> {

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

    public UTF16_CodePoint(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy) {
        super(downstream, errorStrategy);
        state = normal;
    }

    public UTF16_CodePoint(IntAcceptor<? extends R> downstream) {
        super(downstream, ErrorStrategy.THROW_EXCEPTION);
        state = normal;
    }

    @Override
    public void acceptInt(int value) {
        state.accept(value);
    }

    @Override
    public boolean isStageComplete() {
        return state == normal;
    }

    private void terminal(int i){
        if (!Character.isLowSurrogate((char)i))
            handleError(i);
        emit(Character.toCodePoint((char)highSurrogate, (char)i));
        state = normal;
    }

}
