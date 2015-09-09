package org.diablitozzz.jera.db;

import org.diablitozzz.jera.db.log.DbLogService;
import org.diablitozzz.jera.db.platform.DbPlatform;

public interface DbConnectionRepository extends AutoCloseable {
    
    @Override
    public void close();
    
    public DbConnectionCloseable getConnection() throws DbException;
    
    public DbLogService getLogService();
    
    public DbPlatform getPlatform();
    
}
