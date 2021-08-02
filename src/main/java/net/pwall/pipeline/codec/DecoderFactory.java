/*
 * @(#) DecoderFactory.java
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

package net.pwall.pipeline.codec;

import java.nio.charset.Charset;

import net.pwall.pipeline.AbstractIntPipeline;
import net.pwall.pipeline.IntAcceptor;

/**
 * Factory class to get decoder pipeline objects for a given encoding.
 *
 * @author  Peter Wall
 */
public class DecoderFactory {

    /**
     * Get a decoder pipeline for the given character set name.
     *
     * @param   charsetName the character set name
     * @param   downstream  the downstream {@link IntAcceptor}
     * @param   <R>         the pipeline result type
     * @return  a pipeline (with UTF-8 as the default)
     */
    public static <R> AbstractIntPipeline<R> getDecoder(String charsetName, IntAcceptor<? extends R> downstream) {
        if (charsetName.equalsIgnoreCase("windows-1252"))
            return new Windows1252_CodePoint<>(downstream);

        if (charsetName.equalsIgnoreCase("ISO-8859-1"))
            return new ISO8859_1_CodePoint<>(downstream);

        if (charsetName.equalsIgnoreCase("ISO-8859-15"))
            return new ISO8859_15_CodePoint<>(downstream);

        if (charsetName.equalsIgnoreCase("US-ASCII"))
            return new ASCII_CodePoint<>(downstream);

        return new UTF8_CodePoint<>(downstream);
    }

    /**
     * Get a decoder pipeline for the given {@link Charset}.
     *
     * @param   charset     the character set name
     * @param   downstream  the downstream {@link IntAcceptor}
     * @param   <R>         the pipeline result type
     * @return  a pipeline (with UTF-8 as the default)
     */
    public static <R> AbstractIntPipeline<R> getDecoder(Charset charset, IntAcceptor<? extends R> downstream) {
        return getDecoder(charset.name(), downstream);
    }

}
