package org.diablitozzz.jera.db.jdbc;

import javax.sql.DataSource;

import org.diablitozzz.jera.db.DbConnectionCloseable;
import org.diablitozzz.jera.db.DbConnectionRepository;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.log.DbLogService;
import org.diablitozzz.jera.db.platform.DbPlatform;

public class DbConnectionRepositoryJdbc implements DbConnectionRepository {
    
    private DataSource dataSource;
    private final DbLogService logService = new DbLogService();
    private DbPlatform platform;

    @Override
    public void close() {
    }
    
    @Override
    public DbConnectionCloseable getConnection() throws DbException {
        final DbConnectionJdbc dbSession = new DbConnectionJdbc(new DbResourceJdbc(this.dataSource), this.logService, this.platform);
        return dbSession;
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public DbLogService getLogService() {
        return this.logService;
    }

    @Override
    public DbPlatform getPlatform() {
        return this.platform;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPlatform(final DbPlatform platform) {
        this.platform = platform;
    }

}
