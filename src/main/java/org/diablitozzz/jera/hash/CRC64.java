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

/**
 * CRC64 checksum calculator based on the polynom specified in ISO 3309. The
 * implementation is based on the following publications:
 *
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Cyclic_redundancy_check</li>
 * <li>http://www.geocities.com/SiliconValley/Pines/8659/crc.htm</li>
 * </ul>
 */
public class CRC64 {

	public static long encode(final byte[] data) {
		long sum = 0;
		for (final byte element : data) {
			final int lookupidx = ((int) sum ^ element) & 0xff;
			sum = (sum >>> 8) ^ CRC64.LOOKUPTABLE[lookupidx];
		}
		return sum;
	}

	public static long encode(final String data) {
		return CRC64.encode(data.getBytes());
	}

	private static final long POLY64REV = 0xd800000000000000L;

	private static final long[] LOOKUPTABLE;

	static {
		LOOKUPTABLE = new long[0x100];
		for (int i = 0; i < 0x100; i++) {
			long v = i;
			for (int j = 0; j < 8; j++) {
				if ((v & 1) == 1) {
					v = (v >>> 1) ^ CRC64.POLY64REV;
				} else {
					v = (v >>> 1);
				}
			}
			CRC64.LOOKUPTABLE[i] = v;
		}
	}

}
