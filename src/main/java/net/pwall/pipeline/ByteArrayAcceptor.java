/*
 * @(#) ByteArrayAcceptor.java
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

package net.pwall.pipeline;

public class ByteArrayAcceptor extends AbstractIntAcceptor<byte[]> {

    public static final int DEFAULT_INITIAL_CAPACITY = 20;

    private byte[] byteArray;
    private int index;

    public ByteArrayAcceptor(int initialCapacity) {
        byteArray = new byte[initialCapacity];
        index = 0;
    }

    public ByteArrayAcceptor() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void acceptInt(int value) throws Exception {
        int len = byteArray.length;
        if (index >= len) {
            int newLen = len + Math.min(len, 4096);
            byte[] newArray = new byte[newLen];
            System.arraycopy(byteArray, 0, newArray, 0, index);
            byteArray = newArray;
        }
        byteArray[index++] = (byte)value;
    }

    @Override
    public byte[] getResult() {
        byte[] result = new byte[index];
        System.arraycopy(byteArray, 0, result, 0, index);
        return result;
    }

    public int length() {
        return index;
    }

    public void clear() {
        index = 0;
    }

}
