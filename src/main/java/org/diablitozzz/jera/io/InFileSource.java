package org.diablitozzz.jera.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InFileSource implements InStreamSource {

	private final File file;

	public InFileSource(final File file) {
		this.file = file;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

}
