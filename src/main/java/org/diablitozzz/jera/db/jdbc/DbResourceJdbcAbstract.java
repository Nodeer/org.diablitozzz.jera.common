package org.diablitozzz.jera.db.jdbc;

import java.sql.Connection;
import java.sql.Statement;

import org.diablitozzz.jera.db.DbException;

abstract class DbResourceJdbcAbstract implements AutoCloseable {
    
    protected static void execute(final Connection connection, final String sql) throws DbException {
        try {
            try (final Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        } catch (final Throwable e) {
            throw new DbException(e);
        }
    }
    
    protected static String getURL(final Connection connection) {
        if (connection == null) {
            return "null";
        }
        try {
            return connection.getMetaData().getURL();
        } catch (final Throwable e) {
            return "error:" + e.getMessage();
        }
    }
    
    private static boolean isConnected(final Connection connection) {
        
        try {
            if (connection.isClosed()) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        try {
            DbResourceJdbcAbstract.execute(connection, "SELECT 1");
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }
    
    /*/
    private static boolean isPostgreSQL(final Connection connection) {
    	try {
    		return connection.getMetaData().getDatabaseProductName().trim().toLowerCase().equals("postgresql");
    	} catch (final Throwable e) {
    		return false;
    	}
    }/*/
    
    private boolean transactionStarted = false;
    
    private Connection connection;
    
    public void begin() throws DbException {
        
        if (this.transactionStarted) {
            throw new DbException("Transaction already started");
        }
        
        try {
            this.beginImpl();
        } catch (final DbException e) {
            throw e;
        } catch (final Throwable e) {
            throw new DbException(e);
        }
        this.transactionStarted = true;
    }
    
    protected abstract void beginImpl() throws Throwable;
    
    @Override
    public void close() {
        
        if (this.connection != null) {
            this.closeConnection(this.connection);
            this.connection = null;
        }
        this.transactionStarted = false;
    }
    
    private void closeConnection(final Connection connection) {
        
        try { //rollback
            if (this.isTransactionStarted()) {
                this.rollbackImpl();
            }
        } catch (final Throwable e) {
        }
        try {
            this.closeImpl(connection);
        } catch (final Throwable e) {
        }
    }
    
    protected abstract void closeImpl(Connection connection);
    
    public void commit() throws DbException {
        
        if (!this.transactionStarted) {
            throw new DbException("Transaction not started");
        }
        try {
            this.commitImpl();
        } catch (final DbException e) {
            throw e;
        } catch (final Throwable e) {
            throw new DbException(e);
        } finally {
            this.transactionStarted = false;
        }
    }
    
    protected abstract void commitImpl() throws Throwable;
    
    protected abstract Connection createConnection() throws Throwable;
    
    public Connection getConnection() throws DbException {
        if (this.transactionStarted) {
            return this.getConnectionUnsafe();
        }
        return this.getConnectionSafe();
    }
    
    protected Connection getConnectionSafe() throws DbException {
        
        if (this.connection == null) {
            try {
                this.connection = this.createConnection();
            } catch (final DbException e) {
                throw e;
            } catch (final Throwable e) {
                throw new DbException(e);
            }
        } else if (!DbResourceJdbcAbstract.isConnected(this.connection)) {
            this.closeConnection(this.connection);
            try {
                this.connection = this.createConnection();
            } catch (final DbException e) {
                throw e;
            } catch (final Throwable e) {
                throw new DbException(e);
            }
        }
        return this.connection;
    }
    
    protected Connection getConnectionUnsafe() throws DbException {
        
        if (this.connection == null) {
            try {
                this.connection = this.createConnection();
            } catch (final DbException e) {
                throw e;
            } catch (final Throwable e) {
                throw new DbException(e);
            }
        }
        
        return this.connection;
    }
    
    public String getURL() {
        try {
            return DbResourceJdbcAbstract.getURL(this.getConnection());
        } catch (final DbException e) {
            return "error:" + e.getMessage();
        }
    }
    
    public boolean isTransactionStarted() {
        return this.transactionStarted;
    }
    
    public void prepare() throws DbException {
        
        if (!this.transactionStarted) {
            throw new DbException("Transaction not started");
        }
        try {
            this.prepareImpl();
        } catch (final DbException e) {
            throw e;
        } catch (final Throwable e) {
            throw new DbException(e);
        }
        
    }
    
    abstract protected void prepareImpl() throws Throwable;
    
    public void rollback() throws DbException {
        
        if (!this.transactionStarted) {
            throw new DbException("Transaction not started");
        }
        try {
            this.rollbackImpl();
        } catch (final DbException e) {
            throw e;
        } catch (final Throwable e) {
            throw new DbException(e);
        } finally {
            this.transactionStarted = false;
        }
    }
    
    abstract protected void rollbackImpl() throws Throwable;
    
}
