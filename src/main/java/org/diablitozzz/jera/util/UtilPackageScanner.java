
package org.diablitozzz.jera.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilPackageScanner {

    final private static String PROTOCOL_FILE = "file";
    final private static String PROTOCOL_JAR = "jar";
    final private static String CLASS_EXT = ".class";

    final private static Pattern CLASS_PATTERN = Pattern.compile("[A-Za-z0-9\\._]*");
    final private String rootLocation;
    final private List<String> findClassNames;
    final private ClassLoader classLoader;
    final private List<Class<?>> findClasses;
    final private String packageName;

    public UtilPackageScanner(final String packageName) {
        this(packageName, null);
    }

    public UtilPackageScanner(final String packageName, final ClassLoader classLoader) {
        this.classLoader = (classLoader != null) ? classLoader : this.getClass().getClassLoader();

        this.packageName = packageName;
        this.rootLocation = packageName.replace('.', '/');
        this.findClassNames = new ArrayList<String>(30);
        this.findClasses = new ArrayList<Class<?>>(30);
    }

    private List<URL> findResources(final String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        final Enumeration<URL> resourceUrls = this.classLoader.getResources(path);
        final List<URL> out = new LinkedList<URL>();
        while (resourceUrls.hasMoreElements()) {
            final URL url = resourceUrls.nextElement();
            out.add(url);
        }
        return out;
    }

    public List<Class<?>> getFindClasses() {
        return this.findClasses;
    }

    private void makeFile(final File file, final File root) throws IOException {
        if (file.isDirectory()) {
            for (final File subFile : file.listFiles()) {
                if (subFile.isDirectory()) {
                    this.makeFile(subFile, root);
                } else {
                    this.matchItem(subFile, root);
                }
            }
        } else {
            this.matchItem(file, root);
        }
    }

    private void makeJar(final URL location) throws IOException {
        final URLConnection con = location.openConnection();
        if (!(con instanceof JarURLConnection)) {
            return;
        }
        final JarURLConnection jarCon = (JarURLConnection) con;
        jarCon.setUseCaches(false);
        final JarFile jarFile = jarCon.getJarFile();

        for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            final JarEntry entry = entries.nextElement();
            final String entryPath = entry.getName();
            this.matchItem(entryPath);
        }
    }

    private void matchItem(final File file, final File root) throws IOException {
        String name = file.getCanonicalPath();
        name = name.replace(root.getCanonicalPath(), "");
        name = this.rootLocation.concat(name);
        this.matchItem(name);
    }

    private void matchItem(final String nameIn) {
        String name = nameIn;
        if (!name.endsWith(UtilPackageScanner.CLASS_EXT)) {
            return;
        }
        name = name.replace(UtilPackageScanner.CLASS_EXT, "").replace('/', '.');

        if (!name.contains(this.packageName)) {
            return;
        }

        final Matcher m = UtilPackageScanner.CLASS_PATTERN.matcher(name);
        if (!m.matches()) {
            return;
        }

        this.findClassNames.add(name);
    }

    public void scan() throws IOException {
        this.findClassNames.clear();
        this.findClasses.clear();

        final List<URL> resources = this.findResources(this.rootLocation);

        for (final URL url : resources) {
            if (url.getProtocol().equals(UtilPackageScanner.PROTOCOL_FILE)) {
                final File root = new File(url.getFile());
                this.makeFile(root, root);
            } else if (url.getProtocol().equals(UtilPackageScanner.PROTOCOL_JAR)) {
                this.makeJar(url);
            }
        }
        for (final String item : this.findClassNames) {
            try {
                final Class<?> cl = Class.forName(item, false, this.classLoader);
                this.findClasses.add(cl);
            } catch (final Throwable e) {
                // class is bad continue
            }
        }
    }

}
