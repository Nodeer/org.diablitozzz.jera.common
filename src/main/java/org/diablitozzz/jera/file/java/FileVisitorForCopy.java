package org.diablitozzz.jera.file.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.diablitozzz.jera.file.FileListener;

class FileVisitorForCopy implements FileVisitor<Path> {

	protected final Path dest;
	protected final Path src;
	protected final boolean override;
	protected final FileListener fileListener;

	public FileVisitorForCopy(Path src, Path dest, boolean override, FileListener fileListener) {

		this.dest = dest;
		this.src = src;
		this.override = override;
		this.fileListener = fileListener;
	}

	protected Path makeDestPath(Path file) {
		Path relative = this.src.relativize(file);
		Path result = this.dest.resolve(relative);
		return result;
	}

	protected void onDir(Path dest) throws IOException {

		if (!Files.exists(dest)) {
			Files.createDirectories(dest);
		}
		// override if dest not dir
		else if (this.override && !Files.isDirectory(dest)) {
			Files.delete(dest);
			Files.createDirectories(dest);
		}
	}

	protected void onFile(Path src, Path dest) throws IOException {

		if (this.override) {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
		}
		else {
			Files.copy(src, dest, StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
		}
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

		Path dest = this.makeDestPath(dir);

		Exception error = null;
		try {
			this.onDir(dest);
		} catch (IOException e) {
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
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

		Path dest = this.makeDestPath(file);

		Exception error = null;

		try {
			this.onFile(file, dest);
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
