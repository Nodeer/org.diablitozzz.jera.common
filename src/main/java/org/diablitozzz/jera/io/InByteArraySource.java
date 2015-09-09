package org.diablitozzz.jera.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InByteArraySource implements InStreamSource {

	private final byte[] data;

	public InByteArraySource(final byte[] data) {
		this.data = data;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.data);
	}

}
