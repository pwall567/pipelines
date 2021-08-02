/*
 * @(#) SwitchableDecoderTest.java
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

package net.pwall.pipeline.codec.test;

import java.nio.charset.Charset;

import net.pwall.pipeline.StringAcceptor;
import net.pwall.pipeline.codec.SwitchableDecoder;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwitchableDecoderTest {

    private static final String utf8String = "UTF-8|\u00C2\u00A9\u00C3\u00B7\u00E2\u0080\u0094";
    private static final String iso88591String = "ISO-8859-1|\u00A9\u00F7\u00B5";
    private static final String windows1252String = "windows-1252|\u00A9\u00F7\u0080";

    @Test
    public void shouldSwitchToUTF8() throws Exception {
        assertEquals("\u00A9\u00F7\u2014", pipeString(utf8String));
        assertEquals("\u00A9\u00F7\u2014", pipeString2(utf8String));
    }

    @Test
    public void shouldSwitchToISO88591() throws Exception {
        assertEquals("\u00A9\u00F7\u00B5", pipeString(iso88591String));
        assertEquals("\u00A9\u00F7\u00B5", pipeString2(iso88591String));
    }

    @Test
    public void shouldSwitchToWindows1252() throws Exception {
        assertEquals("\u00A9\u00F7\u20AC", pipeString(windows1252String));
        assertEquals("\u00A9\u00F7\u20AC", pipeString2(windows1252String));
    }

    private String pipeString(String string) throws Exception {
        StringAcceptor stringAcceptor = new StringAcceptor();
        SwitchableDecoder<String> switchableDecoder = new SwitchableDecoder<>(stringAcceptor);
        for (int i = 0, n = string.length(); i < n; i++) {
            char ch = string.charAt(i);
            if (ch == '|') {
                switchableDecoder.switchTo(stringAcceptor.getResult());
                stringAcceptor.clear();
            }
            else
                switchableDecoder.accept(ch);
        }
        switchableDecoder.close();
        return switchableDecoder.getResult();
    }

    private String pipeString2(String string) throws Exception {
        StringAcceptor stringAcceptor = new StringAcceptor();
        SwitchableDecoder<String> switchableDecoder = new SwitchableDecoder<>(stringAcceptor);
        for (int i = 0, n = string.length(); i < n; i++) {
            char ch = string.charAt(i);
            if (ch == '|') {
                switchableDecoder.switchTo(Charset.forName(stringAcceptor.getResult()));
                stringAcceptor.clear();
            }
            else
                switchableDecoder.accept(ch);
        }
        switchableDecoder.close();
        return switchableDecoder.getResult();
    }

}
