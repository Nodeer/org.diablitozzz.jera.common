package org.diablitozzz.jera.temporary;

import java.io.IOException;
import java.io.InputStream;

import org.diablitozzz.jera.io.InByteArraySource;
import org.diablitozzz.jera.io.InStreamSource;
import org.diablitozzz.jera.io.IoUtils;

public class TemporaryResourceInMemory extends TemporaryResourceAbstract {

	private byte[] data;
	private boolean closed = false;

	public TemporaryResourceInMemory() {
		this.data = new byte[0];
	}

	public TemporaryResourceInMemory(final byte[] data) {
		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
	}

	public TemporaryResourceInMemory(final InputStream source) throws IOException {

		try {
			this.data = IoUtils.toByteArray(source);
		} catch (final IOException e) {
			this.close();
			throw e;
		} finally {
			IoUtils.closeQuietly(source);
		}
	}

	public TemporaryResourceInMemory(final InStreamSource source) throws IOException {

		try (InputStream sourceStream = source.getInputStream()) {
			this.data = IoUtils.toByteArray(sourceStream);
		} catch (final IOException e) {
			this.close();
			throw e;
		}
	}

	@Override
	public void close() {
		this.closed = true;
		this.data = null;
	}

	@Override
	public InStreamSource getInputStreamSource() {
		this.assertClosed();
		return new InByteArraySource(this.data);
	}

	@Override
	public long getLength() {
		this.assertClosed();
		return this.data.length;
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}
}
