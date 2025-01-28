/*
 * @(#) package-info.java
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

/**
 * <h1>UTF-16 encoding and decoding classes</h1>
 *
 * <p>Classes that encode UTF-16 data (<i>e.g.</i> Java strings) to a byte encoding, or decode such encodings to
 * UTF-16.
 * Also included are classes to encode / decode to or from Unicode code points.</p>
 *
 * <p>These classes all use the {@link io.jstuff.pipeline.IntPipeline IntPipeline} interface, which includes methods for
 * accepting characters (as {@code int}) and emitting converted characters (one-to-one, one-to-many or many-to one).</p>
 *
 * @author  Peter Wall
 */

package io.jstuff.pipeline.codec;
