package org.diablitozzz.jera.file;

import java.io.IOException;

import org.diablitozzz.jera.file.java.FileSessionRepositoryJava;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestFileSession {

    private final static TestFileSessionImpl impl = new TestFileSessionImpl();
    private final static FileSessionRepositoryJava fileSessionRepositoryJava = new FileSessionRepositoryJava();
    
    @DataProvider(name = "all")
    private Object[][] providerAll() throws FileException {
        return new Object[][] {
                this.providerJava()[0]
        };
    }

    @DataProvider(name = "java")
    private Object[][] providerJava() throws FileException {
        return new Object[][] { { TestFileSession.fileSessionRepositoryJava.getSession() } };
    }

    @Test(dataProvider = "all")
    public void testContains(final FileSession session) throws FileException {
        TestFileSession.impl.testContains(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testContainsTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testContains(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirDir(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyDirDir(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirDirTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyDirDir(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirFileNotOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyDirFileNotOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirFileNotOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyDirFileNotOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirFileOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyDirFileOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyDirFileOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyDirFileOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileDirNotOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyFileDirNotOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileDirNotOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyFileDirNotOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileFileNotOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyFileFileNotOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileFileNotOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyFileFileNotOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileFileOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testCopyFileFileOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCopyFileFileOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCopyFileFileOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateDirAndFile(final FileSession session) throws FileException {
        TestFileSession.impl.testCreateDirAndFile(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateDirAndFileTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testCreateDirAndFile(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateInputStream(final FileSession session) throws FileException, IOException {
        TestFileSession.impl.testCreateInputStream(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateInputStreamTx(final FileSession session) throws FileException, IOException {
        session.begin();
        TestFileSession.impl.testCreateInputStream(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateOutputStream(final FileSession session) throws FileException, IOException {
        TestFileSession.impl.testCreateOutputStream(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testCreateOutputStreamTx(final FileSession session) throws FileException, IOException {
        session.begin();
        TestFileSession.impl.testCreateOutputStream(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testGetSize(final FileSession session) throws FileException {
        TestFileSession.impl.testGetSize(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testGetSizeTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testGetSize(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testIsDirectory(final FileSession session) throws FileException {
        TestFileSession.impl.testIsDirectory(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testIsDirectoryTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testIsDirectory(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testIsFile(final FileSession session) throws FileException {
        TestFileSession.impl.testIsFile(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testIsFileTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testIsFile(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testIsTransactionStarted(final FileSession session) throws FileException {

        Assert.assertFalse(session.isTransactionStarted());

        session.begin();
        Assert.assertTrue(session.isTransactionStarted());
        session.prepare();
        Assert.assertTrue(session.isTransactionStarted());
        session.rollback();
        Assert.assertFalse(session.isTransactionStarted());

        session.begin();
        Assert.assertTrue(session.isTransactionStarted());
        session.commit();
        Assert.assertFalse(session.isTransactionStarted());

        session.begin();
        Assert.assertTrue(session.isTransactionStarted());
        session.close();
        //Assert.assertFalse(session.isTransactionStarted());
        //session.close();

    }

    @Test(dataProvider = "all")
    public void testMoveDirDir(final FileSession session) throws FileException {
        TestFileSession.impl.testMoveDirDir(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testMoveDirDirTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testMoveDirDir(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testMoveDirFileWithOverride(final FileSession session) throws FileException {
        TestFileSession.impl.testMoveDirFileWithOverride(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testMoveDirFileWithOverrideTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testMoveDirFileWithOverride(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testScan(final FileSession session) throws FileException {
        TestFileSession.impl.testScan(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testScanTx(final FileSession session) throws FileException {
        session.begin();
        TestFileSession.impl.testScan(session);
        session.rollback();
        session.close();
    }

    @Test(dataProvider = "all")
    public void testTruncate(final FileSession session) throws FileException, IOException {
        TestFileSession.impl.testTruncate(session);
        session.close();
    }

    @Test(dataProvider = "all")
    public void testTruncateTx(final FileSession session) throws FileException, IOException {
        session.begin();
        TestFileSession.impl.testTruncate(session);
        session.rollback();
        session.close();
    }

}
