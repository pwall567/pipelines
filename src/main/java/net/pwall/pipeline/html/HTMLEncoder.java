/*
 * @(#) HTMLEncoder.java
 *
 * pipelines   Pipeline conversion library for Java
 * Copyright (c) 2021, 2022, 2023 Peter Wall
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

package net.pwall.pipeline.html;

import net.pwall.pipeline.IntAcceptor;
import net.pwall.pipeline.xxml.MappingEntry;
import net.pwall.pipeline.codec.EncoderBase;

/**
 * HTML encoder - encode text using HTML escaping.
 *
 * @author  Peter Wall
 * @param   <R>     the pipeline result type
 */
public class HTMLEncoder<R> extends EncoderBase<R> {

    // these are the character entity references from 0xA0 to 0xFF
    public static final String[] baseEntities = {
            "nbsp", "iexcl", "cent", "pound", "curren", "yen", "brvbar", "sect",
            "uml", "copy", "ordf", "laquo", "not", "shy", "reg", "macr",
            "deg", "plusmn", "sup2", "sup3", "acute", "micro", "para", "middot",
            "cedil", "sup1", "ordm", "raquo", "frac14", "frac12", "frac34", "iquest",
            "Agrave", "Aacute", "Acirc", "Atilde", "Auml", "Aring", "AElig", "Ccedil",
            "Egrave", "Eacute", "Ecirc", "Euml", "Igrave", "Iacute", "Icirc", "Iuml",
            "ETH", "Ntilde", "Ograve", "Oacute", "Ocirc", "Otilde", "Ouml", "times",
            "Oslash", "Ugrave", "Uacute", "Ucirc", "Uuml", "Yacute", "THORN", "szlig",
            "agrave", "aacute", "acirc", "atilde", "auml", "aring", "aelig", "ccedil",
            "egrave", "eacute", "ecirc", "euml", "igrave", "iacute", "icirc", "iuml",
            "eth", "ntilde", "ograve", "oacute", "ocirc", "otilde", "ouml", "divide",
            "oslash", "ugrave", "uacute", "ucirc", "uuml", "yacute", "thorn", "yuml"
    };

    // these entities are non-contiguous, so they are looked up by a binary search
    public static final MappingEntry[] mappedEntities = {
            new MappingEntry(0x0152, "OElig"),
            new MappingEntry(0x0153, "oelig"),
            new MappingEntry(0x0160, "Scaron"),
            new MappingEntry(0x0161, "scaron"),
            new MappingEntry(0x0178, "Yuml"),
            new MappingEntry(0x0192, "fnof"),
            new MappingEntry(0x02C6, "circ"),
            new MappingEntry(0x02DC, "tilde"),
            new MappingEntry(0x0391, "Alpha"),
            new MappingEntry(0x0392, "Beta"),
            new MappingEntry(0x0393, "Gamma"),
            new MappingEntry(0x0394, "Delta"),
            new MappingEntry(0x0395, "Epsilon"),
            new MappingEntry(0x0396, "Zeta"),
            new MappingEntry(0x0397, "Eta"),
            new MappingEntry(0x0398, "Theta"),
            new MappingEntry(0x0399, "Iota"),
            new MappingEntry(0x039A, "Kappa"),
            new MappingEntry(0x039B, "Lambda"),
            new MappingEntry(0x039C, "Mu"),
            new MappingEntry(0x039D, "Nu"),
            new MappingEntry(0x039E, "Xi"),
            new MappingEntry(0x039F, "Omicron"),
            new MappingEntry(0x03A0, "Pi"),
            new MappingEntry(0x03A1, "Rho"),
            new MappingEntry(0x03A3, "Sigma"),
            new MappingEntry(0x03A4, "Tau"),
            new MappingEntry(0x03A5, "Upsilon"),
            new MappingEntry(0x03A6, "Phi"),
            new MappingEntry(0x03A7, "Chi"),
            new MappingEntry(0x03A8, "Psi"),
            new MappingEntry(0x03A9, "Omega"),
            new MappingEntry(0x03B1, "alpha"),
            new MappingEntry(0x03B2, "beta"),
            new MappingEntry(0x03B3, "gamma"),
            new MappingEntry(0x03B4, "delta"),
            new MappingEntry(0x03B5, "epsilon"),
            new MappingEntry(0x03B6, "zeta"),
            new MappingEntry(0x03B7, "eta"),
            new MappingEntry(0x03B8, "theta"),
            new MappingEntry(0x03B9, "iota"),
            new MappingEntry(0x03BA, "kappa"),
            new MappingEntry(0x03BB, "lambda"),
            new MappingEntry(0x03BC, "mu"),
            new MappingEntry(0x03BD, "nu"),
            new MappingEntry(0x03BE, "xi"),
            new MappingEntry(0x03BF, "omicron"),
            new MappingEntry(0x03C0, "pi"),
            new MappingEntry(0x03C1, "rho"),
            new MappingEntry(0x03C2, "sigmaf"),
            new MappingEntry(0x03C3, "sigma"),
            new MappingEntry(0x03C4, "tau"),
            new MappingEntry(0x03C5, "upsilon"),
            new MappingEntry(0x03C6, "phi"),
            new MappingEntry(0x03C7, "chi"),
            new MappingEntry(0x03C8, "psi"),
            new MappingEntry(0x03C9, "omega"),
            new MappingEntry(0x03D1, "thetasym"),
            new MappingEntry(0x03D2, "upsih"),
            new MappingEntry(0x03D6, "piv"),
            new MappingEntry(0x2002, "ensp"),
            new MappingEntry(0x2003, "emsp"),
            new MappingEntry(0x2009, "thinsp"),
            new MappingEntry(0x200C, "zwnj"),
            new MappingEntry(0x200D, "zwj"),
            new MappingEntry(0x200E, "lrm"),
            new MappingEntry(0x200F, "rlm"),
            new MappingEntry(0x2013, "ndash"),
            new MappingEntry(0x2014, "mdash"),
            new MappingEntry(0x2018, "lsquo"),
            new MappingEntry(0x2019, "rsquo"),
            new MappingEntry(0x201A, "sbquo"),
            new MappingEntry(0x201C, "ldquo"),
            new MappingEntry(0x201D, "rdquo"),
            new MappingEntry(0x201E, "bdquo"),
            new MappingEntry(0x2020, "dagger"),
            new MappingEntry(0x2021, "Dagger"),
            new MappingEntry(0x2022, "bull"),
            new MappingEntry(0x2026, "hellip"),
            new MappingEntry(0x2030, "permil"),
            new MappingEntry(0x2032, "prime"),
            new MappingEntry(0x2033, "Prime"),
            new MappingEntry(0x2039, "lsaquo"),
            new MappingEntry(0x203A, "rsaquo"),
            new MappingEntry(0x203E, "oline"),
            new MappingEntry(0x2044, "frasl"),
            new MappingEntry(0x20AC, "euro"),
            new MappingEntry(0x2111, "image"),
            new MappingEntry(0x2118, "weierp"),
            new MappingEntry(0x211C, "real"),
            new MappingEntry(0x2122, "trade"),
            new MappingEntry(0x2135, "alefsym"),
            new MappingEntry(0x2190, "larr"),
            new MappingEntry(0x2191, "uarr"),
            new MappingEntry(0x2192, "rarr"),
            new MappingEntry(0x2193, "darr"),
            new MappingEntry(0x2194, "harr"),
            new MappingEntry(0x21B5, "crarr"),
            new MappingEntry(0x21D0, "lArr"),
            new MappingEntry(0x21D1, "uArr"),
            new MappingEntry(0x21D2, "rArr"),
            new MappingEntry(0x21D3, "dArr"),
            new MappingEntry(0x21D4, "hArr"),
            new MappingEntry(0x2200, "forall"),
            new MappingEntry(0x2202, "part"),
            new MappingEntry(0x2203, "exist"),
            new MappingEntry(0x2205, "empty"),
            new MappingEntry(0x2207, "nabla"),
            new MappingEntry(0x2208, "isin"),
            new MappingEntry(0x2209, "notin"),
            new MappingEntry(0x220B, "ni"),
            new MappingEntry(0x220F, "prod"),
            new MappingEntry(0x2211, "sum"),
            new MappingEntry(0x2212, "minus"),
            new MappingEntry(0x2217, "lowast"),
            new MappingEntry(0x221A, "radic"),
            new MappingEntry(0x221D, "prop"),
            new MappingEntry(0x221E, "infin"),
            new MappingEntry(0x2220, "ang"),
            new MappingEntry(0x2227, "and"),
            new MappingEntry(0x2228, "or"),
            new MappingEntry(0x2229, "cap"),
            new MappingEntry(0x222A, "cup"),
            new MappingEntry(0x222B, "int"),
            new MappingEntry(0x2234, "there4"),
            new MappingEntry(0x223C, "sim"),
            new MappingEntry(0x2245, "cong"),
            new MappingEntry(0x2248, "asymp"),
            new MappingEntry(0x2260, "ne"),
            new MappingEntry(0x2261, "equiv"),
            new MappingEntry(0x2264, "le"),
            new MappingEntry(0x2265, "ge"),
            new MappingEntry(0x2282, "sub"),
            new MappingEntry(0x2283, "sup"),
            new MappingEntry(0x2284, "nsub"),
            new MappingEntry(0x2286, "sube"),
            new MappingEntry(0x2287, "supe"),
            new MappingEntry(0x2295, "oplus"),
            new MappingEntry(0x2297, "otimes"),
            new MappingEntry(0x22A5, "perp"),
            new MappingEntry(0x22C5, "sdot"),
            new MappingEntry(0x2308, "lceil"),
            new MappingEntry(0x2309, "rceil"),
            new MappingEntry(0x230A, "lfloor"),
            new MappingEntry(0x230B, "rfloor"),
            new MappingEntry(0x2329, "lang"),
            new MappingEntry(0x232A, "rang"),
            new MappingEntry(0x25CA, "loz"),
            new MappingEntry(0x2660, "spades"),
            new MappingEntry(0x2663, "clubs"),
            new MappingEntry(0x2665, "hearts"),
            new MappingEntry(0x2666, "diams")
    };

    public HTMLEncoder(IntAcceptor<? extends R> downstream) {
        super(downstream);
    }

    @Override
    public void acceptInt(int value) {
        if (value == '"')
            emit("&quot;");
        else if (value == '&')
            emit("&amp;");
        else if (value == '<')
            emit("&lt;");
        else if (value == '>')
            emit("&gt;");
        else if (value >= ' ' && value < 0x7F)
            emit(value);
        else if (value >= 0xA0 && value <= 0xFF) {
            emit('&');
            emit(baseEntities[value - 0xA0]);
            emit(';');
        }
        else {
            int hi = mappedEntities.length;
            if (value >= mappedEntities[0].getCodePoint() && value <= mappedEntities[hi - 1].getCodePoint()) {
                int lo = 0;
                while (lo < hi) {
                    int mid = (lo + hi) >>> 1;
                    MappingEntry entry = mappedEntities[mid];
                    int entryCodePoint = entry.getCodePoint();
                    if (value == entryCodePoint) {
                        emit('&');
                        emit(entry.getString());
                        emit(';');
                        return;
                    }
                    if (value < entryCodePoint)
                        hi = mid;
                    else
                        lo = mid + 1;
                }
            }
            emit("&#x");
            emitHex(value);
            emit(';');
        }
    }

}
