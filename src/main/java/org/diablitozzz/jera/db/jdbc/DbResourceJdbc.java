package org.diablitozzz.jera.db.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.diablitozzz.jera.db.DbException;

class DbResourceJdbc extends DbResourceJdbcAbstract {
    
    private final DataSource dataSource;
    
    public DbResourceJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    protected void beginImpl() throws Throwable {
        this.getConnectionSafe().setAutoCommit(false);
    }
    
    @Override
    protected void closeImpl(final Connection connection) {
        try {
            connection.close();
        } catch (final Throwable e) {
        
        }
    }
    
    @Override
    protected void commitImpl() throws Throwable {
        
        try {
            this.getConnectionUnsafe().commit();
        } catch (final Throwable e) {
            throw e;
        } finally {
            this.getConnectionUnsafe().setAutoCommit(true);
        }
        
    }
    
    @Override
    protected Connection createConnection() throws Throwable {
        
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
        } catch (final Throwable e) {
            throw new DbException("Can't create connection", e);
        }
        return connection;
    }
    
    @Override
    protected void prepareImpl() throws Throwable {
        DbResourceJdbcAbstract.execute(this.getConnectionUnsafe(), "SELECT 1");
    }
    
    @Override
    protected void rollbackImpl() throws Throwable {
        
        final Connection connection = this.getConnectionUnsafe();
        try {
            connection.rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (final Throwable e) {
            }
        }
    }
}
