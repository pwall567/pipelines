/*
 * @(#) ForkPipelineTest.java
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

package net.pwall.pipeline;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ForkPipelineTest {

    @Test
    public void shouldForkPipeline() {
        ListAcceptor<String> listAcceptor1 = new ListAcceptor<>();
        Filter<String, List<String>> filter1 = new Filter<>(listAcceptor1, a -> a.startsWith("A"));
        ListAcceptor<String> listAcceptor2 = new ListAcceptor<>();
        Filter<String, List<String>> filter2 = new Filter<>(listAcceptor2, a -> !a.startsWith("A"));
        ForkPipeline<String, List<String>> fork = new ForkPipeline<>(filter1, filter2);
        fork.accept("Abc");
        fork.accept("def");
        fork.accept("AAA");
        fork.accept("BBB");
        List<String> list1 = filter1.getResult();
        assertEquals(2, list1.size());
        assertEquals("Abc", list1.get(0));
        assertEquals("AAA", list1.get(1));
        List<String> list2 = filter2.getResult();
        assertEquals(2, list2.size());
        assertEquals("def", list2.get(0));
        assertEquals("BBB", list2.get(1));
    }

}
