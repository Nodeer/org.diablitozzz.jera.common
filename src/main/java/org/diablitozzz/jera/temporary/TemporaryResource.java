package org.diablitozzz.jera.temporary;

import java.io.IOException;

import org.diablitozzz.jera.io.InStreamSource;

public interface TemporaryResource extends AutoCloseable {

	@Override
	void close();

	String getChecksum() throws IOException;

	long getChecksumAdler32() throws IOException;

	InStreamSource getInputStreamSource();

	long getLength();

	boolean isClosed();
}
