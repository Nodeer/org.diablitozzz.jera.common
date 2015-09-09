package org.diablitozzz.jera.file.java;

import org.diablitozzz.jera.file.FileException;
import org.diablitozzz.jera.file.FileSession;
import org.diablitozzz.jera.file.FileSessionRepository;

public class FileSessionRepositoryJava implements FileSessionRepository {
    
    @Override
    public void close() throws Exception {
    }
    
    @Override
    public FileSession getSession() throws FileException {
        
        final FileSession fileSession = new FileSessionJava();
        return fileSession;
    }
    
}
