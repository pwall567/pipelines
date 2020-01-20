/*
 * @(#) StringAcceptor.java
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
 * An {@link Acceptor} that creates a {@link String} from a sequence of {@code int} characters.
 *
 * @author  Peter Wall
 */
public class StringAcceptor extends AbstractIntAcceptor<String> {

    public static final int DEFAULT_INITIAL_CAPACITY = 20;

    private StringBuilder stringBuilder;

    public StringAcceptor(int initialCapacity) {
        stringBuilder = new StringBuilder(initialCapacity);
    }

    public StringAcceptor() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void acceptInt(int value) {
        stringBuilder.append((char)value);
    }

    @Override
    public String getResult() {
        return stringBuilder.toString();
    }

    public int length() {
        return stringBuilder.length();
    }

}
