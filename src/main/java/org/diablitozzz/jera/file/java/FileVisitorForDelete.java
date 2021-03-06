package org.diablitozzz.jera.file.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.diablitozzz.jera.file.FileListener;

class FileVisitorForDelete implements FileVisitor<Path> {

	private final FileListener fileListener;

	public FileVisitorForDelete(FileListener fileListener) {
		this.fileListener = fileListener;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

		Exception error = null;
		try {
			Files.delete(dir);
		} catch (Exception e) {
			error = e;
		}

		//fileListener
		if (this.fileListener != null) {
			if (error == null) {
				this.fileListener.onDirectoryDone(dir.toString());
			}
			else {
				this.fileListener.onDirectoryFail(dir.toString(), error);
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

		Exception error = null;
		try {
			Files.delete(file);
		} catch (Exception e) {
			error = e;
		}

		//fileListener
		if (this.fileListener != null) {
			if (error == null) {
				this.fileListener.onFileDone(file.toString());
			}
			else {
				this.fileListener.onFileFail(file.toString(), error);
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

}
