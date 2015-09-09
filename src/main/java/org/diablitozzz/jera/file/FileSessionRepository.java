package org.diablitozzz.jera.file;

public interface FileSessionRepository extends AutoCloseable {
    
    public FileSession getSession() throws FileException;
}
