package org.diablitozzz.jera.temporary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.diablitozzz.jera.io.InFileSource;
import org.diablitozzz.jera.io.InStreamSource;
import org.diablitozzz.jera.io.IoUtils;

public class TemporaryResourceAsFile extends TemporaryResourceAbstract {

	private File file;
	private final static AtomicInteger tempSequence = new AtomicInteger();

	public static File creatTempFile(final File tmpDir) {

		final StringBuilder builder = new StringBuilder();
		builder.append(TemporaryResourceAsFile.class.getCanonicalName());
		builder.append('-');
		builder.append(System.nanoTime());
		builder.append('-');
		builder.append(TemporaryResourceAsFile.tempSequence.incrementAndGet());
		builder.append(".tmp");
		return new File(tmpDir, builder.toString());
	}

	public static File getTempDir() {
		final File tempDir = new File(System.getProperty("java.io.tmpdir"));
		if (!tempDir.isDirectory()) {
			throw new IllegalStateException("System property java.io.tmpdir not exists or not a directory");
		}
		return tempDir;
	}

	public TemporaryResourceAsFile() {
		this(TemporaryResourceAsFile.getTempDir());
	}

	public TemporaryResourceAsFile(final File tmpDir) {
		this.file = TemporaryResourceAsFile.creatTempFile(tmpDir);
	}

	public TemporaryResourceAsFile(final File tmpDir, final InputStream source) throws IOException {

		this.file = TemporaryResourceAsFile.creatTempFile(tmpDir);
		try {
			IoUtils.copy(source, this.file);
		} catch (final IOException e) {
			this.close();
			throw e;
		} finally {
			IoUtils.closeQuietly(source);
		}
	}

	public TemporaryResourceAsFile(final File tmpDir, final InStreamSource source) throws IOException {

		this.file = TemporaryResourceAsFile.creatTempFile(tmpDir);
		try (InputStream sourceStream = source.getInputStream()) {
			IoUtils.copy(sourceStream, this.file);
		} catch (final IOException e) {
			this.close();
			throw e;
		}
	}

	public TemporaryResourceAsFile(final InputStream source) throws IOException {
		this(TemporaryResourceAsFile.getTempDir(), source);
	}

	public TemporaryResourceAsFile(final InStreamSource source) throws IOException {
		this(TemporaryResourceAsFile.getTempDir(), source);
	}

	@Override
	public void close() {

		this.assertClosed();
		try {
			this.file.delete();
		} catch (final Throwable e) {
		}
		this.file = null;
	}

	public File getFile() {
		this.assertClosed();
		return this.file;
	}

	@Override
	public InStreamSource getInputStreamSource() {
		this.assertClosed();
		return new InFileSource(this.file);
	}

	@Override
	public long getLength() {
		this.assertClosed();
		return this.file.length();
	}

	@Override
	public boolean isClosed() {
		return this.file == null;
	}

}