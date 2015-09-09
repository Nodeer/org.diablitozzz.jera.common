package org.diablitozzz.jera.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class UtilFile {

	public static List<File> scanFiles(final File root) throws IOException {

		final List<File> out = new ArrayList<File>();
		final Path rootPath = Paths.get(root.toURI());
		Files.walkFileTree(rootPath, new FileVisitor<Path>() {

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				out.add(file.toFile());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
		return out;

	}

	public static void scanFiles(final List<File> out, final File root, final FileFilter filter) {

		if (root.isDirectory()) {

			final File[] files = root.listFiles();
			for (final File file : files) {

				if (file.isDirectory()) {
					UtilFile.scanFiles(out, file, filter);
				}
				else if (filter.accept(file)) {
					out.add(file);
				}
			}
		}
		else {
			if (filter.accept(root)) {
				out.add(root);
			}
		}
	}

}
