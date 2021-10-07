/*
 * @(#) DynamicReaderTest.java
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

package net.pwall.pipeline.codec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import net.pwall.pipeline.ByteArrayAcceptor;
import net.pwall.pipeline.buffer.CharSequencePipeline;

public class DynamicReaderTest {

    private static final String basic = "basic string";
    private static final String resume = "R\u00E9sum\u00E9";
    private static final String complex = "\u201C \u00A9 complex \u2014 \u20AC1000\u201D";

    @Test
    public void shouldPassSimpleStringThroughUnmodified() throws Exception {
        byte[] data = convertToUTF8(basic);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(basic, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseUTF8() throws Exception {
        byte[] data = convertToUTF8(resume);
        assertEquals(8, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(resume, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseWindows1252() throws Exception {
        byte[] data = convertToWindows1252(resume);
        assertEquals(6, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(resume, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseUTF8Complex() throws Exception {
        byte[] data = convertToUTF8(complex);
        assertEquals(29, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(complex, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseWindows1252Complex() throws Exception {
        byte[] data = convertToWindows1252(complex);
        assertEquals(20, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(complex, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseUTF16BE() throws Exception {
        byte[] data = convertToUTF16BE(basic, false);
        assertEquals(24, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(basic, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseUTF16BEWithBOM() throws Exception {
        byte[] data = convertToUTF16BE(basic, true);
        assertEquals(26, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(basic, readFully(dynamic));
    }

    @Test
    public void shouldRecogniseComplexUTF16BEWithBOM() throws Exception {
        byte[] data = convertToUTF16BE(complex, true);
        assertEquals(42, data.length);
        InputStream in = new ByteArrayInputStream(data);
        Reader dynamic = new DynamicReader(in);
        assertEquals(complex, readFully(dynamic));
    }

    private static byte[] convertToUTF8(String str) throws Exception {
        System.out.println(str);
        CharSequencePipeline<byte[]> utf8 = new CharSequencePipeline<>(new CodePoint_UTF8<>(new ByteArrayAcceptor()));
        utf8.accept(str);
        utf8.close();
        return utf8.getResult();
    }

    private static byte[] convertToWindows1252(String str) throws Exception {
        System.out.println(str);
        CharSequencePipeline<byte[]> win1252 =
                new CharSequencePipeline<>(new CodePoint_Windows1252<>(new ByteArrayAcceptor()));
        win1252.accept(str);
        win1252.close();
        return win1252.getResult();
    }

    private static byte[] convertToUTF16BE(String str, boolean bom) {
        int n = str.length();
        byte[] result = new byte[n * 2 + (bom ? 2 : 0)];
        int x = 0;
        if (bom) {
            result[x++] = (byte)0xFE;
            result[x++] = (byte)0xFF;
        }
        for (int i = 0; i < n; i++) {
            int ch = str.charAt(i);
            result[x++] = (byte)(ch >> 8);
            result[x++] = (byte)ch;
        }
        return result;
    }

    private static String readFully(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int ch = reader.read();
            if (ch < 0)
                break;
            sb.append((char)ch);
        }
        return sb.toString();
    }

}
