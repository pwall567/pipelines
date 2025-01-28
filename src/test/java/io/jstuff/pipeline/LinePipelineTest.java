/*
 * @(#) LinePipelineTest.java
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

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LinePipelineTest {

    @Test
    public void shouldSplitTextOnNewlines() {
        String input = "abc\ndef\nghi\n";
        ListAcceptor<String> listAcceptor = new ListAcceptor<>();
        try (LinePipeline<List<String>> pipeline = new LinePipeline<>(listAcceptor)) {
            pipeline.accept(input);
        }
        List<String> list = listAcceptor.getResult();
        assertEquals(3, list.size());
        assertEquals("abc", list.get(0));
        assertEquals("def", list.get(1));
        assertEquals("ghi", list.get(2));
    }

    @Test
    public void shouldSplitTextOnCRLF() {
        String input = "abc\r\ndef\r\nghi\r\n";
        ListAcceptor<String> listAcceptor = new ListAcceptor<>();
        try (LinePipeline<List<String>> pipeline = new LinePipeline<>(listAcceptor)) {
            pipeline.accept(input);
        }
        List<String> list = listAcceptor.getResult();
        assertEquals(3, list.size());
        assertEquals("abc", list.get(0));
        assertEquals("def", list.get(1));
        assertEquals("ghi", list.get(2));
    }

    @Test
    public void shouldAllowMissingLineTerminatorAtEnd() {
        String input = "abc\nde";
        ListAcceptor<String> listAcceptor = new ListAcceptor<>();
        try (LinePipeline<List<String>> pipeline = new LinePipeline<>(listAcceptor)) {
            pipeline.accept(input);
        }
        List<String> list = listAcceptor.getResult();
        assertEquals(2, list.size());
        assertEquals("abc", list.get(0));
        assertEquals("de", list.get(1));
    }

    @Test
    public void shouldApplyMaximumLength() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        ListAcceptor<String> listAcceptor = new ListAcceptor<>();
        try (LinePipeline<List<String>> pipeline = new LinePipeline<>(listAcceptor, 4)) {
            pipeline.accept(input);
        }
        List<String> list = listAcceptor.getResult();
        assertEquals(7, list.size());
        assertEquals("abcd", list.get(0));
        assertEquals("efgh", list.get(1));
        assertEquals("ijkl", list.get(2));
        assertEquals("mnop", list.get(3));
        assertEquals("qrst", list.get(4));
        assertEquals("uvwx", list.get(5));
        assertEquals("yz", list.get(6));
    }

}
