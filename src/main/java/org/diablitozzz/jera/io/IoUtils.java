package org.diablitozzz.jera.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

	private static int DEFAULT_BUFFER_SIZE = 1024;

	public static void closeQuietly(final Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (final Throwable ioe) {
			// ignore
		}
	}

	public static long copy(final InputStream input, final File output) throws IOException {

		try (FileOutputStream outStream = new FileOutputStream(output)) {
			return IoUtils.copy(input, outStream);
		}
	}

	public static long copy(final InputStream input, final OutputStream output)
			throws IOException {
		final byte[] buffer = new byte[IoUtils.DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] toByteArray(final InputStream input) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		IoUtils.copy(input, output);
		return output.toByteArray();
	}

}
