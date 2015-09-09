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
package org.diablitozzz.jera.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class UtilIO {

    /**
     * Закрывает поток
     */
    public static void closeForce(final Closeable closeAble) {
        if (closeAble != null) {
            try {
                closeAble.close();
            } catch (final IOException e) {
            }
        }
    }

    /**
     * Копирует из  inputStream -> outputStream
     *
     * @param inputStream
     * @param outputStream
     * @param bufferSizeBytes - размер буффера в байтах
     * @param maxSizeBytes - максимальный размер в байтах
     * @param timeOutMs - таймаут в миллисекундах
     */
    public static void copyStream(final InputStream inputStream, final OutputStream outputStream, final int bufferSizeBytes, final int maxSizeBytes, final long timeOutMs) throws IOException {

        final long start = System.currentTimeMillis();
        try {

            byte[] buffer = null;
            int totalBytesRead = 0;
            int bytesRead = 0;

            while (true) {
                if ((System.currentTimeMillis() - start) > timeOutMs) {
                    throw new IOException("TimeOut " + timeOutMs + "ms");
                }

                buffer = new byte[bufferSizeBytes];
                bytesRead = inputStream.read(buffer);
                //данные пока не пришли
                if (bytesRead == 0) {

                    try {
                        TimeUnit.MICROSECONDS.sleep(1);
                        continue;
                    } catch (final InterruptedException e) {
                        throw new IOException(e);
                    }
                }
                //данных нет
                if (bytesRead == -1) {
                    break;
                }
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                if (totalBytesRead > maxSizeBytes) {
                    throw new IOException("Data too large");
                }
            }
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(outputStream);
        } finally {
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(outputStream);
        }
    }

    /**
     * Возвращает содержимое url
     */
    public static byte[] download(final InputStream inputStream, final int bufferSizeBytes, final int maxSizeBytes, final long timeOutMs) throws IOException {

        ByteArrayOutputStream writer = null;
        try {
            writer = new ByteArrayOutputStream(bufferSizeBytes * 2);
            UtilIO.copyStream(inputStream, writer, bufferSizeBytes, maxSizeBytes, timeOutMs);
            final byte[] out = writer.toByteArray();
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(writer);
            return out;
        } finally {
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(writer);
        }
    }

    /**
     * Возвращает содержимое url
     */
    public static byte[] download(final URL url, final int bufferSizeBytes, final int maxSizeBytes, final long timeOutMs) throws IOException {

        InputStream reader = null;
        ByteArrayOutputStream writer = null;
        try {
            reader = url.openStream();
            writer = new ByteArrayOutputStream(bufferSizeBytes * 2);
            UtilIO.copyStream(reader, writer, bufferSizeBytes, maxSizeBytes, timeOutMs);
            return writer.toByteArray();
        } finally {
            UtilIO.closeForce(reader);
            UtilIO.closeForce(writer);
        }
    }

    /**
     * Закачивает файл
     */
    public static void downloadFile(final InputStream inputStream, final File file, final int bufferSizeBytes, final int maxSizeBytes, final long timeOutMs) throws IOException {

        OutputStream writer = null;
        try {
            writer = new FileOutputStream(file);
            UtilIO.copyStream(inputStream, writer, bufferSizeBytes, maxSizeBytes, timeOutMs);

        } finally {
            UtilIO.closeForce(inputStream);
            UtilIO.closeForce(writer);
        }
    }

    /**
     * Закачивает файл
     */
    public static void downloadFile(final URL url, final File file, final int bufferSizeBytes, final int maxSizeBytes, final long timeOutMs) throws IOException {

        InputStream reader = null;
        OutputStream writer = null;
        try {
            reader = url.openStream();
            writer = new FileOutputStream(file);
            UtilIO.copyStream(reader, writer, bufferSizeBytes, maxSizeBytes, timeOutMs);

        } finally {
            UtilIO.closeForce(reader);
            UtilIO.closeForce(writer);
        }
    }

    /**
     * Сохранить файл в строку
     */
    public static String fileToString(final File file, final Charset charset) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return UtilIO.inputStreamToString(inputStream, charset);
        }
    }

    public static InputStream getInputStreamByPath(final String path) throws FileNotFoundException {

        final File file = new File(path);
        InputStream inputStream = null;

        if (file.exists()) {
            inputStream = new FileInputStream(file);
        } else {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream(path);
        }

        if (inputStream == null) {
            throw new FileNotFoundException("File not found " + path);
        }
        return inputStream;
    }

    public static byte[] inputStreamToByteArray(final InputStream in) throws IOException {
        return UtilIO.inputStreamToByteArray(in, 1024);
    }

    public static byte[] inputStreamToByteArray(final InputStream in, final int bufferSize) throws IOException {

        byte data[] = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);

        try {

            int next = in.read();
            while (next > -1) {
                out.write(next);
                next = in.read();
            }
            out.flush();
            data = out.toByteArray();

        } finally {
            UtilIO.closeForce(out);
            UtilIO.closeForce(in);
        }

        return data;
    }

    public static String inputStreamToString(final InputStream in, final Charset charset) throws IOException {
        return new String(UtilIO.inputStreamToByteArray(in), charset);
    }

    public static byte[] readBlob(final File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return UtilIO.inputStreamToByteArray(is);
        }
    }

    public static byte[] readBlob(final String path) throws IOException {

        final InputStream inStream = UtilIO.getInputStreamByPath(path);
        byte[] out;
        try {
            out = UtilIO.inputStreamToByteArray(inStream);
        } finally {
            UtilIO.closeForce(inStream);
        }
        return out;
    }

    /**
     * Сохранить blob в файл
     */
    public static void saveBlob(final File file, final byte[] blob) throws IOException {

        final FileOutputStream outStream = new FileOutputStream(file);
        try {
            outStream.write(blob);
        } catch (final IOException e) {
            throw e;
        } finally {
            UtilIO.closeForce(outStream);
        }
    }

}
