package org.diablitozzz.jera.file.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.diablitozzz.jera.file.FileSession;

class FileVisitorForScan implements FileVisitor<Path> {

	private final org.diablitozzz.jera.file.FileVisitor fileVisitor;
	private final FileSession fileSession;

	public FileVisitorForScan(org.diablitozzz.jera.file.FileVisitor fileVisitor, FileSession fileSession) {
		this.fileVisitor = fileVisitor;
		this.fileSession = fileSession;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

		try {
			return this.fileVisitor.afterDirectory(dir.toString(), this.fileSession) ?
					FileVisitResult.CONTINUE :
					FileVisitResult.TERMINATE;
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);

		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

		try {
			return this.fileVisitor.beforeDirectory(dir.toString(), this.fileSession) ?
					FileVisitResult.CONTINUE :
					FileVisitResult.TERMINATE;
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);

		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		try {
			return this.fileVisitor.onFile(file.toString(), this.fileSession) ?
					FileVisitResult.CONTINUE :
					FileVisitResult.TERMINATE;
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);

		}
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException cause) throws IOException {

		try {
			return this.fileVisitor.onError(file.toString(), this.fileSession, cause) ?
					FileVisitResult.CONTINUE :
					FileVisitResult.TERMINATE;
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e);
		}
	}

}
