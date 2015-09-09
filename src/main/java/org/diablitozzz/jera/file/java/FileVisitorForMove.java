package org.diablitozzz.jera.file.java;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.diablitozzz.jera.file.FileListener;

class FileVisitorForMove extends FileVisitorForCopy {

	public FileVisitorForMove(Path src, Path dest, boolean override, FileListener fileListener) {
		super(src, dest, override, fileListener);
	}

	@Override
	protected void onFile(Path src, Path dest) throws IOException {

		if (this.override) {
			Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
		}
		else {
			Files.move(src, dest, LinkOption.NOFOLLOW_LINKS);
		}
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

		try {
			Files.delete(dir);
		} catch (IOException error) {
			if (this.fileListener != null) {
				this.fileListener.onDirectoryFail(dir.toString(), error);
			}
		}
		return FileVisitResult.CONTINUE;
	}

}
