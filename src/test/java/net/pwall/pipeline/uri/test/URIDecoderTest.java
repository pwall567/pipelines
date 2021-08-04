package net.pwall.pipeline.uri.test;

import org.junit.Test;

import net.pwall.pipeline.IntPipeline;
import net.pwall.pipeline.StringAcceptor;
import net.pwall.pipeline.uri.URIDecoder;
import static org.junit.Assert.assertEquals;

public class URIDecoderTest {

    @Test
    public void shouldDecodePlainStringUnmodified() throws Exception {
        IntPipeline<String> pipeline1 = new URIDecoder<>(new StringAcceptor());
        pipeline1.accept("plain");
        assertEquals("plain", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIDecoder<>(new StringAcceptor());
        pipeline2.accept("aMuchLongerString");
        assertEquals("aMuchLongerString", pipeline2.getResult());
    }

    @Test
    public void shouldDecodePercentSequences() throws Exception {
        IntPipeline<String> pipeline1 = new URIDecoder<>(new StringAcceptor());
        pipeline1.accept("Hello%2C%20World%21");
        assertEquals("Hello, World!", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIDecoder<>(new StringAcceptor());
        pipeline2.accept("a%20more-complicated%20string%3A%20a%2Fb%2Bc%25e.%28%3F%3F%3F%29");
        assertEquals("a more-complicated string: a/b+c%e.(???)", pipeline2.getResult());
    }

    @Test
    public void shouldDecodePlusAsSpace() throws Exception {
        IntPipeline<String> pipeline1 = new URIDecoder<>(new StringAcceptor());
        pipeline1.accept("Hello%2C+World%21");
        assertEquals("Hello, World!", pipeline1.getResult());
        IntPipeline<String> pipeline2 = new URIDecoder<>(new StringAcceptor());
        pipeline2.accept("a+more-complicated+string%3A+a%2Fb%2Bc%25e.%28%3F%3F%3F%29");
        assertEquals("a more-complicated string: a/b+c%e.(???)", pipeline2.getResult());
    }

}
