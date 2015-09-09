package org.diablitozzz.jera.temporary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import org.diablitozzz.jera.io.IoUtils;

abstract public class TemporaryResourceAbstract implements TemporaryResource {

	protected void assertClosed() {
		if (this.isClosed()) {
			throw new IllegalStateException("Resource is closed");
		}
	}

	public void copyTo(final OutputStream out) throws IOException {

		try (InputStream in = this.getInputStreamSource().getInputStream()) {
			IoUtils.copy(in, out);
		}
	}

	@Override
	public String getChecksum() throws IOException {
		return Long.toHexString(this.getChecksumAdler32());
	}

	@Override
	public long getChecksumAdler32() throws IOException {

		this.assertClosed();

		final Adler32 adler32 = new Adler32();
		try (final InputStream inputStream = this.getInputStreamSource().getInputStream()) {

			try (final CheckedInputStream cis = new CheckedInputStream(inputStream, adler32)) {

				//read
				final byte[] buffer = new byte[1024];
				while (cis.read(buffer) >= 0) {
				}
				return cis.getChecksum().getValue();
			}
		}
	}
}
