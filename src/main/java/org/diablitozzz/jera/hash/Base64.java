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

import java.nio.charset.Charset;

public class Base64 {

    public static byte[] decode(byte[] pArray)
    {
        Base64Codec base64 = new Base64Codec();
        return base64.decode(pArray);
    }

    public static String decode(String data)
    {
        return Base64.decode(data, Charset.defaultCharset(), false);
    }

    public static String decode(String data, boolean urlSafe)
    {
        return Base64.decode(data, Charset.defaultCharset(), urlSafe);
    }

    public static String decode(String data, Charset encoding, boolean urlSafe)
    {
        Base64Codec base64 = new Base64Codec();
        base64.setUrlSafe(urlSafe);
        byte[] out = base64.decode(data.getBytes(encoding));
        return new String(out, encoding);
    }

    public static byte[] encode(byte[] pArray)
    {
        Base64Codec base64 = new Base64Codec();
        return base64.encode(pArray);
    }

    public static String encode(byte[] pArray, Charset encoding) {
        Base64Codec base64 = new Base64Codec();
        byte[] out = base64.encode(pArray);
        return new String(out, encoding).trim();
    }

    public static String encode(String data)
    {
        return Base64.encode(data, Charset.defaultCharset(), false);
    }

    public static String encode(String data, boolean urlSafe)
    {
        return Base64.encode(data, Charset.defaultCharset(), urlSafe);
    }

    public static String encode(String data, Charset encoding, boolean urlSafe)
    {
        Base64Codec base64 = new Base64Codec();
        base64.setUrlSafe(urlSafe);
        byte[] out = base64.encode(data.getBytes(encoding));
        return new String(out, encoding).trim();
    }

    public static boolean isBase64(byte octet)
    {
        Base64Codec base64 = new Base64Codec();
        return base64.isBase64(octet);
    }

    public static boolean isBase64(byte[] arrayOctet)
    {
        Base64Codec base64 = new Base64Codec();
        return base64.isBase64(arrayOctet);
    }

    public static boolean isBase64(String data)
    {
        return Base64.isBase64(data, Charset.defaultCharset());
    }

    public static boolean isBase64(String data, Charset encoding)
    {
        byte[] bytes = data.getBytes(encoding);
        return Base64.isBase64(bytes);
    }

}
