package org.diablitozzz.jera.file.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.diablitozzz.jera.file.FileException;
import org.diablitozzz.jera.file.FileListener;
import org.diablitozzz.jera.file.FileSession;
import org.diablitozzz.jera.file.FileVisitor;

public class FileSessionJava implements FileSession {

	private boolean transactionStarted = false;

	public FileSessionJava() {
	}

	@Override
	public void begin() throws FileException {

		if (this.transactionStarted) {
			throw new FileException("Transaction already started");
		}
		this.transactionStarted = true;
	}

	@Override
	public void close() {
		if (this.isTransactionStarted()) {
			try {
				this.rollback();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void commit() throws FileException {

		if (!this.transactionStarted) {
			throw new FileException("Transaction not started");
		}
		this.transactionStarted = false;
	}

	@Override
	public boolean contains(String path) throws FileException {
		return this.contains(path, false);
	}

	@Override
	public boolean contains(String path, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		return Files.exists(Paths.get(path));
	}

	@Override
	public void copy(String src, String dest, boolean override) throws FileException {
		this.copy(src, dest, override, null);
	}

	@Override
	public void copy(String src, String dest, boolean override, FileListener fileListener) throws FileException {

		Path srcP = Paths.get(src);

		if (!Files.exists(srcP)) {
			throw new FileException("Can't copy src not exists " + src);
		}

		Path destP = Paths.get(dest);
		try {
			FileVisitorForCopy copyVisitor = new FileVisitorForCopy(srcP, destP, override, fileListener);
			Files.walkFileTree(srcP, copyVisitor);
		} catch (IOException e) {
			throw new FileException("Can't copy." + src + " -> " + dest, e);
		}
	}

	@Override
	public void createDirectory(String path) throws FileException {

		Path dir = Paths.get(path);

		if (Files.exists(dir)) {
			throw new FileException("File exists" + path);
		}
		try {
			Files.createDirectories(dir);
		} catch (IOException e) {
			throw new FileException("Can't create directory " + path, e);
		}

	}

	@Override
	public void createFile(String path) throws FileException {

		Path file = Paths.get(path);

		if (Files.exists(file)) {
			throw new FileException("File exists" + path);
		}
		try {
			Files.createFile(file);
		} catch (IOException e) {
			throw new FileException("Can't create file " + path, e);
		}
	}

	@Override
	public InputStream createInputStream(String path) throws FileException {
		return this.createInputStream(path, false);
	}

	@Override
	public InputStream createInputStream(String path, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}

		try {
			return Files.newInputStream(Paths.get(path));
		} catch (IOException e) {
			throw new FileException("Can't create input stream " + path, e);
		}
	}

	@Override
	public OutputStream createOutputStream(String path) throws FileException {
		return this.createOutputStream(path, false);
	}

	@Override
	public OutputStream createOutputStream(String path, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		try {
			return Files.newOutputStream(Paths.get(path));
		} catch (IOException e) {
			throw new FileException("Can't create output stream " + path, e);
		}
	}

	@Override
	public void delete(String path) throws FileException {
		this.delete(path, null);
	}

	@Override
	public void delete(String path, FileListener fileListener) throws FileException {

		Path root = Paths.get(path);

		if (!Files.exists(root)) {
			if (fileListener != null) {
				fileListener.onDirectoryFail(path, new FileNotFoundException());
			}
			return;
		}
		FileVisitorForDelete visitorForDelete = new FileVisitorForDelete(fileListener);
		try {

			Files.walkFileTree(root, visitorForDelete);
		} catch (IOException e) {
			throw new FileException("Can't delete " + path, e);
		}
	}

	@Override
	public long getSize(String path) throws FileException {
		return this.getSize(path, false);
	}

	@Override
	public long getSize(String path, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		Path item = Paths.get(path);
		if (!Files.exists(item)) {
			throw new FileException("File not exists " + path);
		}

		try {
			FileVisitorForSize sizeVisitor = new FileVisitorForSize();
			Files.walkFileTree(item, sizeVisitor);
			return sizeVisitor.getSize();
		} catch (IOException e) {
			throw new FileException("Can't get  size " + path, e);
		}
	}

	@Override
	public boolean isDirectory(String path) throws FileException {
		return this.isDirectory(path, false);
	}

	@Override
	public boolean isDirectory(String path, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		return Files.isDirectory(Paths.get(path));
	}

	@Override
	public boolean isFile(String path) throws FileException {
		return this.isFile(path, false);
	}

	@Override
	public boolean isFile(String path, boolean lockExclusively) throws FileException {
		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		return Files.isRegularFile(Paths.get(path));
	}

	@Override
	public boolean isTransactionStarted() {
		return this.transactionStarted;
	}

	@Override
	public void move(String src, String dest, boolean override) throws FileException {
		this.move(src, dest, override, null);
	}

	@Override
	public void move(String src, String dest, boolean override, FileListener fileListener) throws FileException {

		Path srcP = Paths.get(src);

		if (!Files.exists(srcP)) {
			throw new FileException("Can't move src not exists " + src);
		}
		Path destP = Paths.get(dest);
		if (override && Files.exists(destP)) {
			this.delete(dest);
		}

		try {
			FileVisitorForMove moveVisitor = new FileVisitorForMove(srcP, destP, override, fileListener);
			Files.walkFileTree(srcP, moveVisitor);
		} catch (IOException e) {
			throw new FileException("Can't move." + src + " -> " + dest, e);
		}
	}

	@Override
	public void prepare() throws FileException {

		if (!this.transactionStarted) {
			throw new FileException("Transaction not started");
		}

	}

	@Override
	public void rollback() throws FileException {

		if (!this.transactionStarted) {
			throw new FileException("Transaction not started");
		}
		this.transactionStarted = false;
	}

	@Override
	public void scan(String path, FileVisitor fileVisitor) throws FileException {
		this.scan(path, fileVisitor, false);
	}

	@Override
	public void scan(String path, FileVisitor fileVisitor, boolean lockExclusively) throws FileException {

		if (lockExclusively) {
			throw new UnsupportedOperationException("Lock not supported");
		}
		Path item = Paths.get(path);
		if (!Files.exists(item)) {
			throw new FileException("File not found " + path);
		}

		try {
			FileVisitorForScan scanVisitor = new FileVisitorForScan(fileVisitor, this);
			Files.walkFileTree(item, scanVisitor);
		} catch (IOException e) {
			throw new FileException("Can't get file list " + path, e);
		}
	}

	@Override
	public void truncate(String path, long newLength) throws FileException {

		try (SeekableByteChannel channel = Files.newByteChannel(Paths.get(path), StandardOpenOption.WRITE)) {
			channel.truncate(newLength);
		} catch (IOException e) {
			throw new FileException("Can't truncate file " + path, e);
		}
	}
}
