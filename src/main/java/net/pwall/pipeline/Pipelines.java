/*
 * @(#) Pipelines.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;

/**
 * A set of static functions for use with the {@code pipelines} library.
 *
 * @author  Peter Wall
 */
public class Pipelines {

    private static final char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static final char[] tensDigits = {
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'
    };

    public static <R> R process(Reader reader, IntAcceptor<R> downstream) throws Exception {
        while (true) {
            int i = reader.read();
            downstream.accept(i);
            if (i < 0)
                break;
        }
        return downstream.getResult();
    }

    public static <R> R process(InputStream inputStream, IntAcceptor<R> downstream) throws Exception {
        while (true) {
            int i = inputStream.read();
            downstream.accept(i);
            if (i < 0)
                break;
        }
        return downstream.getResult();
    }

    public static <R> R process(File file, IntAcceptor<R> downstream) throws Exception {
        return process(new FileInputStream(file), downstream);
    }

    public static <A, R> R process(Iterable<A> iterable, Acceptor<A, R> downstream) throws Exception {
        for (A item : iterable)
            downstream.accept(item);
        downstream.close();
        return downstream.getResult();
    }

    public static void acceptInt(IntAcceptor<?> acceptor, int i) throws Exception {
        if (i < 0) {
            if (i == Integer.MIN_VALUE)
                acceptor.accept("-2147483648");
            else {
                acceptor.accept('-');
                acceptPositiveInt(acceptor, -i);
            }
        }
        else
            acceptPositiveInt(acceptor, i);
    }

    public static void acceptPositiveInt(IntAcceptor<?> acceptor, int i) throws Exception {
        if (i >= 100) {
            int n = i / 100;
            acceptPositiveInt(acceptor, n);
            i -= n * 100;
            acceptor.accept(tensDigits[i]);
            acceptor.accept(digits[i]);
        }
        else if (i >= 10) {
            acceptor.accept(tensDigits[i]);
            acceptor.accept(digits[i]);
        }
        else
            acceptor.accept(digits[i]);
    }

}
