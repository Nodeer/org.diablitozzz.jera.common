package org.diablitozzz.jera.file;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileSession extends AutoCloseable {
    
    public void begin() throws FileException;
    
    @Override
    public void close();
    
    public void commit() throws FileException;
    
    public boolean contains(String path) throws FileException;
    
    public boolean contains(String path, boolean lockExclusively) throws FileException;
    
    public void copy(String src, String dest, boolean override) throws FileException;
    
    public void copy(String src, String dest, boolean override, FileListener fileListener) throws FileException;
    
    public void createDirectory(String path) throws FileException;
    
    public void createFile(String path) throws FileException;
    
    public InputStream createInputStream(String path) throws FileException;
    
    public InputStream createInputStream(String path, boolean lockExclusively) throws FileException;
    
    public OutputStream createOutputStream(String path) throws FileException;
    
    public OutputStream createOutputStream(String path, boolean lockExclusively) throws FileException;
    
    public void delete(String path) throws FileException;
    
    public void delete(String path, FileListener fileListener) throws FileException;
    
    public long getSize(String path) throws FileException;
    
    public long getSize(String path, boolean lockExclusively) throws FileException;
    
    public boolean isDirectory(String path) throws FileException;
    
    public boolean isDirectory(String path, boolean lockExclusively) throws FileException;
    
    public boolean isFile(String path) throws FileException;
    
    public boolean isFile(String path, boolean lockExclusively) throws FileException;
    
    public boolean isTransactionStarted() throws FileException;
    
    public void move(String src, String dest, boolean override) throws FileException;
    
    public void move(String src, String dest, boolean override, FileListener fileListener) throws FileException;
    
    public void prepare() throws FileException;
    
    public void rollback() throws FileException;
    
    public void scan(String path, FileVisitor fileVisitor) throws FileException;
    
    public void scan(String path, FileVisitor fileVisitor, boolean lockExclusively) throws FileException;
    
    public void truncate(String path, long newLength) throws FileException;
    
}
