/*
 * @(#) HTMLEncoderTest.java
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

package io.jstuff.pipeline.html;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.pipeline.IntPipeline;
import io.jstuff.pipeline.StringAcceptor;

public class HTMLEncoderTest {

    @Test
    public void shouldEncodePlainStringUnmodified() {
        IntPipeline<String> pipeline1 = new HTMLEncoder<>(new StringAcceptor());
        pipeline1.accept("plain");
        assertEquals("plain", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new HTMLEncoder<>(new StringAcceptor());
        pipeline2.accept("aMuchLongerString");
        assertEquals("aMuchLongerString", pipeline2.getResult());
    }

    @Test
    public void shouldEncodeSpecialCharacters() {
        IntPipeline<String> pipeline1 = new HTMLEncoder<>(new StringAcceptor());
        pipeline1.accept("<div>hello</div>");
        assertEquals("&lt;div&gt;hello&lt;/div&gt;", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new HTMLEncoder<>(new StringAcceptor());
        pipeline2.accept("<div class=\"test\">It's OK &amp; working</div>");
        assertEquals("&lt;div class=&quot;test&quot;&gt;It's OK &amp;amp; working&lt;/div&gt;", pipeline2.getResult());
    }

    @Test
    public void shouldEncodeNamedSpecialCharacters() {
        IntPipeline<String> pipeline1 = new HTMLEncoder<>(new StringAcceptor());
        pipeline1.accept("<div>\u00A1hol\u00E1!</div>");
        assertEquals("&lt;div&gt;&iexcl;hol&aacute;!&lt;/div&gt;", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new HTMLEncoder<>(new StringAcceptor());
        pipeline2.accept("<div>Even \u2014 more</div>");
        assertEquals("&lt;div&gt;Even &mdash; more&lt;/div&gt;", pipeline2.getResult());
    }

    @Test
    public void shouldEncodeNonstandardSpecialCharacters() {
        IntPipeline<String> pipeline1 = new HTMLEncoder<>(new StringAcceptor());
        pipeline1.accept("<div>M\u0101ori\u0007</div>");
        assertEquals("&lt;div&gt;M&#x101;ori&#x7;&lt;/div&gt;", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new HTMLEncoder<>(new StringAcceptor());
        pipeline2.accept("\uFEFFBOM \u2E19 \u20A4");
        assertEquals("&#xFEFF;BOM &#x2E19; &#x20A4;", pipeline2.getResult());
    }

}
