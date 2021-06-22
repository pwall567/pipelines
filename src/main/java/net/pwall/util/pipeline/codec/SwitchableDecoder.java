package net.pwall.util.pipeline.codec;

import java.nio.charset.Charset;

import net.pwall.util.pipeline.AbstractIntPipeline;
import net.pwall.util.pipeline.IntAcceptor;
import net.pwall.util.pipeline.IntPipeline;

/**
 * A switchable decoder that allows the encoding of an input stream to be determined from the data in the early part of
 * the stream.
 *
 * <p>An XML file declares its encoding on the prefix line, and an HTML can contain a META tag specifying the content
 * type and encoding of the data.  These rely on the declaration occurring early in the file, before any characters
 * outside the base ASCII set are expected to be encountered.</p>
 *
 * <p>This decoder allows the reading of an input stream to commence in ASCII decoding, switching to the full decoding
 * once the declaration has been read.</p>
 *
 * @author  Peter Wall
 * @param   <R>     the "Result" class (may be {@link Void})
 */
public class SwitchableDecoder<R> extends AbstractIntPipeline<R> {

    private final IntAcceptor<R> downstream;
    private IntPipeline<R> delegate;

    public SwitchableDecoder(IntAcceptor<R> downstream) {
        super(downstream);
        this.downstream = downstream;
        delegate = null;
    }

    @Override
    public void acceptInt(int value) throws Exception {
        if (delegate != null)
            delegate.accept(value);
        else
            emit(value);
    }

    public void switchTo(Charset charset) {
        delegate = DecoderFactory.getDecoder(charset, downstream);
    }

    public void switchTo(String charsetName) {
        delegate = DecoderFactory.getDecoder(charsetName, downstream);
    }

}
