package org.diablitozzz.jera.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class UtilCompress {

	public static byte[] compress(final byte[] data, final int level) throws IOException {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length)) {
			try (DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater(level))) {
				dos.write(data);
				dos.flush();
				dos.close();
			}
			return baos.toByteArray();
		}
	}

	public static byte[] decompress(final byte[] compress) throws IOException {

		try (ByteArrayInputStream bais = new ByteArrayInputStream(compress)) {

			try (InflaterInputStream iis = new InflaterInputStream(bais)) {
				return UtilIO.inputStreamToByteArray(iis);
			}
		}
	}

}
