/*
 * @(#) SetAcceptor.java
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

package io.jstuff.pipeline;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An {@link Acceptor} that creates a {@link Set} from a sequence of objects.
 *
 * @author  Peter Wall
 */
public class SetAcceptor<A> extends AbstractAcceptor<A, Set<A>> {

    public static final int DEFAULT_INITIAL_CAPACITY = 10;

    private final Set<A> set;

    public SetAcceptor(int initialCapacity) {
        set = new HashSet<>(initialCapacity);
    }

    public SetAcceptor() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public SetAcceptor(Set<A> set) {
        this.set = set;
    }

    @Override
    public void acceptObject(A value) {
        set.add(value);
    }

    @Override
    public Set<A> getResult() {
        return Collections.unmodifiableSet(set);
    }

    public int getSize() {
        return set.size();
    }

}
