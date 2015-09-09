package org.diablitozzz.jera.db;

import java.util.Collection;

import org.diablitozzz.jera.func.FuncGetWithException;

public class DbConnectionLazy implements DbConnectionCloseable {

    private final FuncGetWithException<DbConnectionCloseable, DbException> connectionFactory;
    private DbConnectionCloseable connection;

    public DbConnectionLazy(final FuncGetWithException<DbConnectionCloseable, DbException> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public int[] batch(final String sql, final Collection<Object[]> paramsList) throws DbException {
        return this.getConnection().batch(sql, paramsList);
    }
    
    @Override
    public int[] batch(final String[] sql) throws DbException {
        return this.getConnection().batch(sql);
    }

    @Override
    public void begin() throws DbException {
        this.getConnection().begin();
    }

    @Override
    public void close() {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    @Override
    public void commit() throws DbException {
        this.getConnection().commit();
    }

    @Override
    public void execute(final DbQuery query) throws DbException {
        this.getConnection().execute(query);
    }

    @Override
    public void execute(final String sql) throws DbException {
        this.getConnection().execute(sql);
    }

    @Override
    public void execute(final String sql, final Object[] params) throws DbException {
        this.getConnection().execute(sql, params);
    }

    private DbConnectionCloseable getConnection() throws DbException {
        if (this.connection == null || !this.connection.isConnected()) {
            this.connection = this.connectionFactory.invoke();
        }
        return this.connection;
    }

    @Override
    public boolean isConnected() {
        try {
            if (this.connection == null) {
                return false;
            }
            return this.connection.isConnected();
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public boolean isTransactionStarted() throws DbException {
        return this.getConnection().isTransactionStarted();
    }

    @Override
    public void prepare() throws DbException {
        this.getConnection().prepare();
    }

    @Override
    public void rollback() throws DbException {
        this.getConnection().rollback();
    }

    @Override
    public DbRecordSet select(final DbQuery query) throws DbException {
        return this.getConnection().select(query);
    }

    @Override
    public DbRecordSet select(final String sql) throws DbException {
        return this.getConnection().select(sql);
    }

    @Override
    public DbRecordSet select(final String sql, final Object[] params) throws DbException {
        return this.getConnection().select(sql, params);
    }

    @Override
    public int update(final DbQuery query) throws DbException {
        return this.getConnection().update(query);
    }

    @Override
    public int update(final String sqlList) throws DbException {
        return this.getConnection().update(sqlList);
    }

    @Override
    public int update(final String sql, final Object[] paramsList) throws DbException {
        return this.getConnection().update(sql, paramsList);
    }
    
}
