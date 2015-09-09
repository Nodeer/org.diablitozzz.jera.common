/*
 * Copyright (c) 2012, diablitozzz.org All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of diablitozzz.org nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.diablitozzz.jera.hash;

import java.awt.image.BufferedImage;
import java.util.BitSet;

public class PHash {

    public static long distance(BitSet s1, BitSet s2)
    {
        long counter = 0;
        int size = s1.length();

        for (int i = 0; i < size; i++) {
            if (s1.get(i) != s2.get(i)) {
                counter++;
            }
        }
        return counter;
    }

    public static double distanceRelative(BitSet s1, BitSet s2)
    {
        long distance = PHash.distance(s1, s2);
        double x = distance / (double) s1.length();
        double y = -100.0d * x + 100.0d;
        return y;
    }

    public static BitSet encode(BufferedImage img)
    {
        PHashCodec codec = new PHashCodec();
        return codec.getHash(img);
    }

    public static long encodeAsLong(BufferedImage img)
    {
        PHashCodec codeck = new PHashCodec();
        BitSet hash = codeck.getHash(img);
        return hash.toLongArray()[0];
    }

    public static String encodeAsString(BufferedImage img)
    {
        PHashCodec codeck = new PHashCodec();
        BitSet hash = codeck.getHash(img);

        int size = hash.length();
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            builder.append(hash.get(i) ? "1" : "0");
        }
        return builder.toString();
    }

}
