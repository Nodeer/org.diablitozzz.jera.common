package org.diablitozzz.jera.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class UtilResource {

	static public URL findFile(String path) throws IOException {

		return UtilResource.findFile(path, Thread.currentThread().getContextClassLoader());
	}

	static public URL findFile(String path, ClassLoader classLoader) throws IOException {

		//search file
		File file = new File(path);
		if (file.exists()) {
			return file.toURI().toURL();
		}

		//search in classPath
		Enumeration<URL> urls = classLoader.getResources(path);

		//фильтруем возврашаем первый file
		while (urls.hasMoreElements()) {
			URL current = urls.nextElement();
			if (current.getProtocol().equals("file")) {
				return current;
			}
		}
		throw new IOException("File not found: " + path);
	}

}
