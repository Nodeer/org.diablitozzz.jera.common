package org.diablitozzz.jera.db.pool;

import org.diablitozzz.jera.db.DbConnectionCloseable;
import org.diablitozzz.jera.db.DbConnectionRepository;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.log.DbLogService;
import org.diablitozzz.jera.db.platform.DbPlatform;
import org.diablitozzz.jera.pool.Pool;
import org.diablitozzz.jera.pool.PoolBasic;
import org.diablitozzz.jera.pool.PoolResourceFactory;
import org.diablitozzz.jera.pool.PoolTime;

public class DbConnectionRepositoryPool implements DbConnectionRepository, PoolResourceFactory<DbConnectionPool> {
    
    private final DbConnectionRepository dbSessionRepository;
    private final PoolBasic<DbConnectionPool> pool;
    
    public DbConnectionRepositoryPool(final DbConnectionRepository dbSessionRepository, final int maxSize, final PoolTime getTimeout) {
        this.dbSessionRepository = dbSessionRepository;
        this.pool = new PoolBasic<DbConnectionPool>(maxSize, this, getTimeout);
    }
    
    @Override
    public void close() {
        this.pool.close();
    }
    
    @Override
    public DbConnectionPool createPoolResource(final Pool<DbConnectionPool> pool) throws DbException {
        return new DbConnectionPool(this.dbSessionRepository, pool);
    }
    
    @Override
    public DbConnectionCloseable getConnection() throws DbException {
        try {
            return this.pool.get();
        } catch (final DbException e) {
            throw e;
        } catch (final Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }
    
    @Override
    public DbLogService getLogService() {
        return this.dbSessionRepository.getLogService();
    }
    
    @Override
    public DbPlatform getPlatform() {
        return this.dbSessionRepository.getPlatform();
    }
    
}
