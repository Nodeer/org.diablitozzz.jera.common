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

public class Base64Codec {

    private static final int MIME_CHUNK_SIZE = 76;
    private static final int MASK_8BITS = 0xff;
    private static final byte PAD_DEFAULT = '=';

    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final byte PAD = Base64Codec.PAD_DEFAULT;

    private final int lineLength;
    private byte[] buffer;
    private int pos;
    private int readPos;
    private boolean eof;
    private int currentLinePos;
    private int modulus;

    private static final int BITS_PER_ENCODED_BYTE = 6;

    private static final int BYTES_PER_UNENCODED_BLOCK = 3;

    private static final int BYTES_PER_ENCODED_BLOCK = 4;

    private static final byte[] CHUNK_SEPARATOR = { '\r', '\n' };

    private static final byte[] STANDARD_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    private static final byte[] URL_SAFE_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    private static final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54,
            55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
            35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    private static final int MASK_6BITS = 0x3f;

    private byte[] encodeTable;

    private final byte[] decodeTable = Base64Codec.DECODE_TABLE;

    private byte[] lineSeparator;

    private final int decodeSize;

    private int encodeSize;

    private int bitWorkArea;

    public Base64Codec()
    {
        int lineLength = Base64Codec.MIME_CHUNK_SIZE;
        byte[] lineSeparator = Base64Codec.CHUNK_SEPARATOR;
        boolean urlSafe = false;
        int encodedBlockSize = Base64Codec.BYTES_PER_ENCODED_BLOCK;
        int chunkSeparatorLength = lineSeparator == null ? 0 : lineSeparator.length;

        this.lineLength = ((lineLength > 0) && (chunkSeparatorLength > 0)) ? (lineLength / encodedBlockSize) * encodedBlockSize : 0;

        if (lineSeparator != null) {
            if (this.containsAlphabetOrPad(lineSeparator)) {
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [\r \n ]");
            }
            if (lineLength > 0) {
                this.encodeSize = Base64Codec.BYTES_PER_ENCODED_BLOCK + lineSeparator.length;
                this.lineSeparator = new byte[lineSeparator.length];
                System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
            } else {
                this.encodeSize = Base64Codec.BYTES_PER_ENCODED_BLOCK;
                this.lineSeparator = null;
            }
        } else {
            this.encodeSize = Base64Codec.BYTES_PER_ENCODED_BLOCK;
            this.lineSeparator = null;
        }
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = urlSafe ? Base64Codec.URL_SAFE_ENCODE_TABLE : Base64Codec.STANDARD_ENCODE_TABLE;
    }

    private int available()
    {
        return this.buffer != null ? this.pos - this.readPos : 0;
    }

    private boolean containsAlphabetOrPad(byte[] arrayOctet)
    {
        if (arrayOctet == null) {
            return false;
        }
        for (byte element : arrayOctet) {
            if ((this.PAD == element) || this.isInAlphabet(element)) {
                return true;
            }
        }
        return false;
    }

    public byte[] decode(byte[] pArray) {
        this.reset();
        if ((pArray == null) || (pArray.length == 0)) {
            return pArray;
        }
        this.decode(pArray, 0, pArray.length);
        this.decode(pArray, 0, -1);
        byte[] result = new byte[this.pos];
        this.readResults(result, 0, result.length);
        return result;
    }

    public void decode(byte[] in, int inPos, int inAvail)
    {
        if (this.eof) {
            return;
        }
        int inPosCur = inPos;

        if (inAvail < 0) {
            this.eof = true;
        }
        for (int i = 0; i < inAvail; i++) {

            this.ensureBufferSize(this.decodeSize);
            byte b = in[inPosCur++];
            if (b == this.PAD) {
                // We're done.
                this.eof = true;
                break;
            }
            if ((b >= 0) && (b < Base64Codec.DECODE_TABLE.length)) {
                int result = Base64Codec.DECODE_TABLE[b];
                if (result >= 0) {
                    this.modulus = (this.modulus + 1) % Base64Codec.BYTES_PER_ENCODED_BLOCK;
                    this.bitWorkArea = (this.bitWorkArea << Base64Codec.BITS_PER_ENCODED_BYTE) + result;
                    if (this.modulus == 0) {
                        this.buffer[this.pos++] = (byte) ((this.bitWorkArea >> 16) & Base64Codec.MASK_8BITS);
                        this.buffer[this.pos++] = (byte) ((this.bitWorkArea >> 8) & Base64Codec.MASK_8BITS);
                        this.buffer[this.pos++] = (byte) (this.bitWorkArea & Base64Codec.MASK_8BITS);
                    }
                }
            }
        }

        if (this.eof && (this.modulus != 0)) {
            this.ensureBufferSize(this.decodeSize);

            switch (this.modulus) {
                case 2:
                    this.bitWorkArea = this.bitWorkArea >> 4;
                    this.buffer[this.pos++] = (byte) ((this.bitWorkArea) & Base64Codec.MASK_8BITS);
                    break;
                case 3:
                    this.bitWorkArea = this.bitWorkArea >> 2;
                    this.buffer[this.pos++] = (byte) ((this.bitWorkArea >> 8) & Base64Codec.MASK_8BITS);
                    this.buffer[this.pos++] = (byte) ((this.bitWorkArea) & Base64Codec.MASK_8BITS);
                    break;
                default:
                    break;
            }
        }
    }

    public byte[] encode(byte[] pArray)
    {
        this.reset();
        if ((pArray == null) || (pArray.length == 0)) {
            return pArray;
        }
        this.encode(pArray, 0, pArray.length);
        this.encode(pArray, 0, -1);
        byte[] buf = new byte[this.pos - this.readPos];
        this.readResults(buf, 0, buf.length);
        return buf;
    }

    public void encode(byte[] in, int inPos, int inAvail)
    {
        if (this.eof) {
            return;
        }
        int inPosCur = inPos;

        if (inAvail < 0) {
            this.eof = true;
            if ((0 == this.modulus) && (this.lineLength == 0)) {
                return;
            }
            this.ensureBufferSize(this.encodeSize);
            int savedPos = this.pos;
            switch (this.modulus) {
                case 1:
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 2) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea << 4) & Base64Codec.MASK_6BITS];
                    if (this.encodeTable == Base64Codec.STANDARD_ENCODE_TABLE) {
                        this.buffer[this.pos++] = this.PAD;
                        this.buffer[this.pos++] = this.PAD;
                    }
                    break;

                case 2:
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 10) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 4) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea << 2) & Base64Codec.MASK_6BITS];

                    if (this.encodeTable == Base64Codec.STANDARD_ENCODE_TABLE) {
                        this.buffer[this.pos++] = this.PAD;
                    }
                    break;
                default:
                    break;
            }
            this.currentLinePos += this.pos - savedPos;
            if ((this.lineLength > 0) && (this.currentLinePos > 0)) {
                System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                this.pos += this.lineSeparator.length;
            }
        } else {
            for (int i = 0; i < inAvail; i++) {
                this.ensureBufferSize(this.encodeSize);
                this.modulus = (this.modulus + 1) % Base64Codec.BYTES_PER_UNENCODED_BLOCK;
                int b = in[inPosCur++];
                if (b < 0) {
                    b += 256;
                }
                this.bitWorkArea = (this.bitWorkArea << 8) + b;
                if (0 == this.modulus) {
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 18) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 12) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[(this.bitWorkArea >> 6) & Base64Codec.MASK_6BITS];
                    this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea & Base64Codec.MASK_6BITS];
                    this.currentLinePos += Base64Codec.BYTES_PER_ENCODED_BLOCK;
                    if ((this.lineLength > 0) && (this.lineLength <= this.currentLinePos)) {
                        System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        this.pos += this.lineSeparator.length;
                        this.currentLinePos = 0;
                    }
                }
            }
        }
    }

    private void ensureBufferSize(int size)
    {
        if ((this.buffer == null) || (this.buffer.length < (this.pos + size))) {
            this.resizeBuffer();
        }
    }

    public boolean isBase64(byte octet) {
        return (octet == Base64Codec.PAD_DEFAULT) || ((octet >= 0) && (octet < Base64Codec.DECODE_TABLE.length) && (Base64Codec.DECODE_TABLE[octet] != -1));
    }

    public boolean isBase64(byte[] arrayOctet) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (!this.isBase64(arrayOctet[i]) && !this.isWhiteSpace(arrayOctet[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isInAlphabet(byte octet) {
        return (octet >= 0) && (octet < this.decodeTable.length) && (this.decodeTable[octet] != -1);
    }

    public boolean isUrlSafe() {
        return this.encodeTable == Base64Codec.URL_SAFE_ENCODE_TABLE;
    }

    private boolean isWhiteSpace(byte byteToCheck)
    {
        switch (byteToCheck) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
                return true;
            default:
                return false;
        }
    }

    private int readResults(byte[] b, int bPos, int bAvail)
    {
        if (this.buffer != null) {
            int len = Math.min(this.available(), bAvail);
            System.arraycopy(this.buffer, this.readPos, b, bPos, len);
            this.readPos += len;
            if (this.readPos >= this.pos) {
                this.buffer = null;
            }
            return len;
        }
        return this.eof ? -1 : 0;
    }

    private void reset()
    {
        this.buffer = null;
        this.pos = 0;
        this.readPos = 0;
        this.currentLinePos = 0;
        this.modulus = 0;
        this.eof = false;
    }

    private void resizeBuffer()
    {
        if (this.buffer == null) {
            this.buffer = new byte[Base64Codec.DEFAULT_BUFFER_SIZE];
            this.pos = 0;
            this.readPos = 0;
        } else {
            byte[] b = new byte[this.buffer.length * Base64Codec.DEFAULT_BUFFER_RESIZE_FACTOR];
            System.arraycopy(this.buffer, 0, b, 0, this.buffer.length);
            this.buffer = b;
        }
    }

    public void setUrlSafe(boolean urlSafe)
    {
        this.encodeTable = urlSafe ? Base64Codec.URL_SAFE_ENCODE_TABLE : Base64Codec.STANDARD_ENCODE_TABLE;
    }

}
