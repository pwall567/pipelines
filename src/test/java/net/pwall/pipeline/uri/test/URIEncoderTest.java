package net.pwall.pipeline.uri.test;

import org.junit.Test;

import net.pwall.pipeline.IntPipeline;
import net.pwall.pipeline.StringAcceptor;
import net.pwall.pipeline.uri.URIEncoder;
import static org.junit.Assert.assertEquals;

public class URIEncoderTest {

    @Test
    public void shouldEncodePlainStringUnmodified() throws Exception {
        IntPipeline<String> pipeline1 = new URIEncoder<>(new StringAcceptor());
        pipeline1.accept("plain");
        assertEquals("plain", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIEncoder<>(new StringAcceptor());
        pipeline2.accept("aMuchLongerString");
        assertEquals("aMuchLongerString", pipeline2.getResult());
    }

    @Test
    public void shouldEncodeReservedCharacters() throws Exception {
        IntPipeline<String> pipeline1 = new URIEncoder<>(new StringAcceptor());
        pipeline1.accept("Hello, World!");
        assertEquals("Hello%2C%20World%21", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIEncoder<>(new StringAcceptor());
        pipeline2.accept("a more-complicated string: a/b+c%e.(???)");
        assertEquals("a%20more-complicated%20string%3A%20a%2Fb%2Bc%25e.%28%3F%3F%3F%29", pipeline2.getResult());
    }

    @Test
    public void shouldEncodeSpaceAsPlusWhenSelected() throws Exception {
        IntPipeline<String> pipeline1 = new URIEncoder<>(true, new StringAcceptor());
        pipeline1.accept("Hello, World!");
        assertEquals("Hello%2C+World%21", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIEncoder<>(true, new StringAcceptor());
        pipeline2.accept("a more-complicated string: a/b+c%e.(???)");
        assertEquals("a+more-complicated+string%3A+a%2Fb%2Bc%25e.%28%3F%3F%3F%29", pipeline2.getResult());
    }

}
