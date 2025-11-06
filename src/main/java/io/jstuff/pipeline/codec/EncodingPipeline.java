/*
 * @(#) EncodingPipeline.java
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

package io.jstuff.pipeline.codec;

import java.util.Arrays;

import io.jstuff.pipeline.IntAcceptor;

/**
 * A pipeline to convert 8-bit character sets based on a reverse mapping table.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class EncodingPipeline<R> extends ErrorHandlingIntPipeline<R> {

    private final int[] reverseTable;

    public EncodingPipeline(IntAcceptor<? extends R> downstream, ErrorStrategy errorStrategy, int[] reverseTable) {
        super(downstream, errorStrategy);
        this.reverseTable = reverseTable;
    }

    @Override
    public void acceptInt(int value) {
        if (value >= 0 && value <= 0x7F)
            emit(value);
        else {
            int lo = 0;
            int hi = reverseTable.length;
            while (lo < hi) {
                int mid = (hi + lo) >>> 1;
                int entry = reverseTable[mid];
                int mapped = entry >>> 16;
                if (mapped < value)
                    lo = mid + 1;
                else if (mapped > value)
                    hi = mid;
                else {
                    emit(entry & 0xFFFF);
                    return;
                }
            }
            handleError(value);
        }
    }

    static int[] createReverseTable(String table) {
        int[] result = new int[table.length()];
        for (int i = 0, n = table.length(); i < n; i++)
            result[i] = (table.charAt(i) << 16) | 0x80 | i;
        Arrays.sort(result);
        return result;
    }

}
