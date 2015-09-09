package org.diablitozzz.jera.file.pool;

import org.diablitozzz.jera.file.FileException;
import org.diablitozzz.jera.file.FileSession;
import org.diablitozzz.jera.file.FileSessionRepository;
import org.diablitozzz.jera.pool.Pool;
import org.diablitozzz.jera.pool.PoolBasic;
import org.diablitozzz.jera.pool.PoolResourceFactory;
import org.diablitozzz.jera.pool.PoolTime;

public class FileSessionRepositoryPool implements FileSessionRepository, PoolResourceFactory<FileSessionPool> {

    private final FileSessionRepository fileSessionRepository;
    private final PoolBasic<FileSessionPool> pool;

    public FileSessionRepositoryPool(final FileSessionRepository fileSessionRepository, final int maxSize, final PoolTime getTimeout) {

        this.fileSessionRepository = fileSessionRepository;
        this.pool = new PoolBasic<FileSessionPool>(maxSize, this, getTimeout);
    }

    @Override
    public void close() {
        this.pool.close();
    }

    @Override
    public FileSessionPool createPoolResource(final Pool<FileSessionPool> pool) throws FileException {
        return new FileSessionPool(this.fileSessionRepository, pool);
    }

    @Override
    public FileSession getSession() throws FileException {

        try {
            return this.pool.get();
        } catch (final FileException e) {
            throw e;
        } catch (final Exception e) {
            throw new FileException(e.getMessage(), e);
        }
    }
    
}
