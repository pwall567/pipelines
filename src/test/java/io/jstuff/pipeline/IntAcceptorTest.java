/*
 * @(#) IntAcceptorTest.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2025 Peter Wall
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IntAcceptorTest {

    @Test
    public void shouldAcceptSingleInt() {
        ListIntAcceptor listIntAcceptor = new ListIntAcceptor();
        listIntAcceptor.accept(123);
        listIntAcceptor.safeClose();
        List<Integer> list = listIntAcceptor.getResult();
        assertEquals(1, list.size());
        assertEquals(123, (int)list.get(0));
    }

    @Test
    public void shouldAcceptListOfInteger() {
        List<Integer> sourceList = new ArrayList<>();
        sourceList.add(123);
        sourceList.add(456);
        sourceList.add(789);
        ListIntAcceptor listIntAcceptor = new ListIntAcceptor();
        listIntAcceptor.accept(sourceList);
        listIntAcceptor.safeClose();
        List<Integer> list = listIntAcceptor.getResult();
        assertEquals(3, list.size());
        assertEquals(123, (int)list.get(0));
        assertEquals(456, (int)list.get(1));
        assertEquals(789, (int)list.get(2));
    }

    @Test
    public void shouldAcceptArrayOfInteger() {
        int[] array = new int[] { 123, 456, 789 };
        ListIntAcceptor listIntAcceptor = new ListIntAcceptor();
        listIntAcceptor.accept(array);
        listIntAcceptor.safeClose();
        List<Integer> list = listIntAcceptor.getResult();
        assertEquals(3, list.size());
        assertEquals(123, (int)list.get(0));
        assertEquals(456, (int)list.get(1));
        assertEquals(789, (int)list.get(2));
    }

}
