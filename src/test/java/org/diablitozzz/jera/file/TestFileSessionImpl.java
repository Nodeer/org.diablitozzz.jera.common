package org.diablitozzz.jera.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

import org.testng.Assert;

class TestFileSessionImpl {
    
    public void testContains(final FileSession session) throws FileException {
        
        final String tmpFile = "/tmp/xadisk.tmpfile";
        final String tmpDir = "/tmp/xadisk.tmpdir";
        
        session.delete(tmpFile);
        session.delete(tmpDir);
        
        Assert.assertFalse(session.contains(tmpFile));
        Assert.assertFalse(session.contains(tmpDir));
        
        session.createFile(tmpFile);
        session.createDirectory(tmpDir);
        
        Assert.assertTrue(session.contains(tmpFile));
        Assert.assertTrue(session.contains(tmpDir));
        
        Assert.assertFalse(session.contains("/tmp/a/a/a/a"));
        
        session.delete(tmpFile);
        session.delete(tmpDir);
    }
    
    public void testCopyDirDir(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createDirectory(tmp + "/from/a");
        session.createDirectory(tmp + "/from/b");
        session.createDirectory(tmp + "/from/c");
        session.createFile(tmp + "/from/from.file");
        session.createFile(tmp + "/from/a/a.file");
        session.createFile(tmp + "/from/b/b.file");
        session.createFile(tmp + "/from/c/c.file");
        
        Assert.assertFalse(session.contains(tmp + "/to/from.file"));
        Assert.assertFalse(session.contains(tmp + "/to/a"));
        Assert.assertFalse(session.contains(tmp + "/to/a/a.file"));
        Assert.assertFalse(session.contains(tmp + "/to/b"));
        Assert.assertFalse(session.contains(tmp + "/to/b/b.file"));
        Assert.assertFalse(session.contains(tmp + "/to/c"));
        Assert.assertFalse(session.contains(tmp + "/to/c/c.file"));
        
        session.copy(tmp + "/from", tmp + "/to", true);
        Assert.assertTrue(session.contains(tmp + "/to/from.file"));
        Assert.assertTrue(session.contains(tmp + "/to/a"));
        Assert.assertTrue(session.contains(tmp + "/to/a/a.file"));
        Assert.assertTrue(session.contains(tmp + "/to/b"));
        Assert.assertTrue(session.contains(tmp + "/to/b/b.file"));
        Assert.assertTrue(session.contains(tmp + "/to/c"));
        Assert.assertTrue(session.contains(tmp + "/to/c/c.file"));
        session.delete(tmp);
    }
    
    public void testCopyDirFileNotOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/to");
        session.copy(tmp + "/from", tmp + "/to", false);
        Assert.assertFalse(session.isDirectory(tmp + "/to"));
        session.delete(tmp + "/to");
    }
    
    public void testCopyDirFileOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/to");
        
        session.copy(tmp + "/from", tmp + "/to", true);
        Assert.assertTrue(session.contains(tmp + "/to"));
        Assert.assertTrue(session.isDirectory(tmp + "/to"));
        session.delete(tmp + "/to");
    }
    
    public void testCopyFileDirNotOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/from/from.file");
        session.createDirectory(tmp + "/to");
        session.copy(tmp + "/from/from.file", tmp + "/to", false);
        Assert.assertFalse(session.isFile(tmp + "/to"));
        session.delete(tmp);
    }
    
    public void testCopyFileFileNotOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/from/from.file");
        
        Assert.assertFalse(session.contains(tmp + "/to"));
        session.copy(tmp + "/from/from.file", tmp + "/to", false);
        Assert.assertTrue(session.contains(tmp + "/to"));
        session.copy(tmp + "/from/from.file", tmp + "/to", false);
        session.delete(tmp);
    }
    
    public void testCopyFileFileOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/from/from.file");
        
        Assert.assertFalse(session.contains(tmp + "/to"));
        session.copy(tmp + "/from/from.file", tmp + "/to", true);
        Assert.assertTrue(session.contains(tmp + "/to"));
        session.copy(tmp + "/from/from.file", tmp + "/to", true);
        
        session.delete(tmp);
    }
    
    public void testCreateDirAndFile(final FileSession session) throws FileException {
        
        final String tmpDirIn = "/tmp/xadisk.tmp/in";
        final String tmpDirOut = "/tmp/xadisk.tmp/out";
        
        session.delete(tmpDirIn);
        session.delete(tmpDirOut);
        
        session.createDirectory(tmpDirIn);
        session.createDirectory(tmpDirOut);
        
        session.createDirectory(tmpDirIn + "/a/a1/a2");
        session.createDirectory(tmpDirIn + "/b/b1/b2");
        session.createDirectory(tmpDirIn + "/c/c1/c2");
        
        session.createFile(tmpDirIn + "/a/a1/a2/a.file");
        session.createFile(tmpDirIn + "/b/b1/b2/a.file");
        session.createFile(tmpDirIn + "/c/c1/c2/a.file");
        
        session.delete(tmpDirIn);
        session.delete(tmpDirOut);
    }
    
    public void testCreateInputStream(final FileSession session) throws FileException, IOException {
        
        final String tmpFile = "/tmp/xadisk.tmpfile";
        session.delete(tmpFile);
        session.createFile(tmpFile);
        try (InputStream inputStream = session.createInputStream(tmpFile)) {
            Assert.assertEquals(inputStream.read(), -1);
        }
        session.delete(tmpFile);
    }
    
    public void testCreateOutputStream(final FileSession session) throws FileException, IOException {
        
        final String tmpFile = "/tmp/xadisk.tmpfile";
        session.delete(tmpFile);
        session.createFile(tmpFile);
        
        //write
        try (OutputStream outputStream = session.createOutputStream(tmpFile)) {
            outputStream.write(1);
        }
        //read
        try (InputStream inputStream = session.createInputStream(tmpFile)) {
            Assert.assertEquals(inputStream.read(), 1);
        }
        session.delete(tmpFile);
    }
    
    public void testGetSize(final FileSession session) throws FileException {
        
        final String tmpFile = "/tmp/xadisk.tmpfile";
        final String tmpDir = "/tmp/xadisk.tmpdir";
        
        session.delete(tmpFile);
        session.delete(tmpDir);
        
        session.createFile(tmpFile);
        session.createDirectory(tmpDir + "/simple.dir");
        session.createFile(tmpDir + "/empty");
        
        Assert.assertTrue(session.contains(tmpFile));
        Assert.assertTrue(session.contains(tmpDir));
        
        Assert.assertEquals(0, session.getSize(tmpFile));
        Assert.assertEquals(0, session.getSize(tmpDir));
        
        Assert.assertTrue(session.isDirectory(tmpDir));
        Assert.assertFalse(session.isDirectory(tmpFile));
        
        Assert.assertFalse(session.isFile(tmpDir));
        Assert.assertTrue(session.isFile(tmpFile));
        
        session.delete(tmpFile);
        session.delete(tmpDir);
        
        Assert.assertFalse(session.contains(tmpFile));
        Assert.assertFalse(session.contains(tmpDir));
    }
    
    public void testIsDirectory(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        session.createDirectory(tmp);
        Assert.assertTrue(session.isDirectory(tmp));
        Assert.assertFalse(session.isDirectory(tmp + "/a/a/a"));
        session.delete(tmp);
    }
    
    public void testIsFile(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        session.createFile(tmp);
        Assert.assertTrue(session.isFile(tmp));
        Assert.assertFalse(session.isFile(tmp + "/a/a/a"));
        session.delete(tmp);
    }
    
    public void testMoveDirDir(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        
        session.createDirectory(tmp + "/from");
        session.createDirectory(tmp + "/from/a");
        session.createDirectory(tmp + "/from/b");
        session.createDirectory(tmp + "/from/c");
        session.createFile(tmp + "/from/from.file");
        session.createFile(tmp + "/from/a/a.file");
        session.createFile(tmp + "/from/b/b.file");
        session.createFile(tmp + "/from/c/c.file");
        
        session.move(tmp + "/from", tmp + "/to", true);
        
        Assert.assertTrue(session.contains(tmp + "/to/from.file"));
        Assert.assertTrue(session.contains(tmp + "/to/a"));
        Assert.assertTrue(session.contains(tmp + "/to/a/a.file"));
        Assert.assertTrue(session.contains(tmp + "/to/b"));
        Assert.assertTrue(session.contains(tmp + "/to/b/b.file"));
        Assert.assertTrue(session.contains(tmp + "/to/c"));
        Assert.assertTrue(session.contains(tmp + "/to/c/c.file"));
        
        Assert.assertFalse(session.contains(tmp + "/from"));
        session.delete(tmp);
    }
    
    public void testMoveDirFileWithOverride(final FileSession session) throws FileException {
        
        final String tmp = "/tmp/xadisk.tmpfile";
        session.delete(tmp);
        session.createDirectory(tmp + "/from");
        session.createFile(tmp + "/to");
        
        Assert.assertTrue(session.isFile(tmp + "/to"));
        session.move(tmp + "/from", tmp + "/to", true);
        Assert.assertFalse(session.isFile(tmp + "/to"));
        Assert.assertFalse(session.contains(tmp + "/from"));
        session.delete(tmp);
    }
    
    public void testScan(final FileSession session) throws FileException {
        
        final String tmpDir = "target" + File.separator + "test" + File.separator + "xadisk.tmpDir";
        session.delete(tmpDir);
        session.createDirectory(tmpDir);
        session.createDirectory(tmpDir + "/a");
        session.createDirectory(tmpDir + "/b");
        session.createFile(tmpDir + "/a/a.file");
        session.createFile(tmpDir + "/b/b.file");
        
        final Collection<String> files = new HashSet<>();
        final FileVisitor fileVisitor = new FileVisitor() {
            
            @Override
            public boolean afterDirectory(final String path, final FileSession session) {
                files.add(path);
                return true;
            }
            
            @Override
            public boolean beforeDirectory(final String path, final FileSession session) {
                files.add(path);
                return true;
            }
            
            @Override
            public boolean onError(final String path, final FileSession session, final Throwable cause) {
                return true;
            }
            
            @Override
            public boolean onFile(final String path, final FileSession session) {
                files.add(path);
                return true;
            }
        };
        
        session.scan(tmpDir, fileVisitor);
        Assert.assertTrue(files.contains(tmpDir + File.separator + "a"));
        Assert.assertTrue(files.contains(tmpDir + File.separator + "b"));
        Assert.assertTrue(files.contains(tmpDir + File.separator + "a" + File.separator + "a.file"));
        Assert.assertTrue(files.contains(tmpDir + File.separator + "b" + File.separator + "b.file"));
        session.delete(tmpDir);
    }
    
    public void testTruncate(final FileSession session) throws FileException, IOException {
        
        final String tmpFile = "/tmp/xadisk.tmpfile.truncate";
        session.delete(tmpFile);
        session.createFile(tmpFile);
        
        //write
        try (OutputStream outputStream = session.createOutputStream(tmpFile)) {
            outputStream.write(1);
            outputStream.write(2);
            outputStream.write(3);
        }
        Assert.assertEquals(session.getSize(tmpFile), 3);
        session.truncate(tmpFile, 1);
        Assert.assertEquals(session.getSize(tmpFile), 1);
        
        session.delete(tmpFile);
    }
    
}
