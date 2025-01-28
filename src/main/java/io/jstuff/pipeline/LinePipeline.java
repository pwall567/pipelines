/*
 * @(#) LinePipeline.java
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

package io.jstuff.pipeline;

/**
 * A pipeline that accepts characters and emits {@link String}s, using LF or CR-LF as line separators.
 *
 * @author  Peter Wall
 * @param   <R>     the result type
 */
public class LinePipeline<R> extends AbstractIntObjectPipeline<String, R> {

    private final int maxLength;
    private final StringBuilder line;
    private boolean crSeen;

    /**
     * Construct a {@code LinePipeline} with the given downstream {@link Acceptor} amd the specified maximum line length.
     *
     * @param downstream    the {@link Acceptor}
     * @param maxLength     the maximum line length
     */
    public LinePipeline(Acceptor<? super String, ? extends R> downstream, int maxLength) {
        super(downstream);
        this.maxLength = maxLength;
        line = new StringBuilder();
        crSeen = false;
    }

    /**
     * Construct a {@code LinePipeline} with the given downstream {@link Acceptor}.
     *
     * @param downstream    the {@link Acceptor}
     */
    public LinePipeline(Acceptor<? super String, ? extends R> downstream) {
        super(downstream);
        maxLength = -1;
        line = new StringBuilder();
        crSeen = false;
    }

    @Override
    public void acceptInt(int value) {
        if (value == '\r') {
            emitLine();
            crSeen = true;
        }
        else if (value == '\n') {
            if (!crSeen)
                emitLine();
            crSeen = false;
        }
        else {
            line.append((char)value);
            if (maxLength > 0 && line.length() >= maxLength)
                emitLine();
            crSeen = false;
        }
    }

    private void emitLine() {
        emit(line.length() == 0 ? "" : line.toString());
        line.setLength(0);
    }

    @Override
    public void close() {
        if (line.length() > 0)
            emitLine(); // emit partial line
        getDownstream().safeClose();
    }

}
