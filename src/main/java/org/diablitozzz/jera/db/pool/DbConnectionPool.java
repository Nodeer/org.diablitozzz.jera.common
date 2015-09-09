package org.diablitozzz.jera.db.pool;

import java.util.Collection;

import org.diablitozzz.jera.db.DbConnectionCloseable;
import org.diablitozzz.jera.db.DbConnectionRepository;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.DbQuery;
import org.diablitozzz.jera.db.DbRecordSet;
import org.diablitozzz.jera.pool.Pool;
import org.diablitozzz.jera.pool.PoolResource;

public class DbConnectionPool implements DbConnectionCloseable, PoolResource {
    
    private DbConnectionCloseable session;
    private boolean pooled;
    private final Pool<DbConnectionPool> pool;
    private final DbConnectionRepository sessionRepository;
    
    public DbConnectionPool(final DbConnectionRepository sessionRepository, final Pool<DbConnectionPool> pool) {
        
        this.session = null;
        this.pool = pool;
        this.pooled = false;
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    public int[] batch(final String sql, final Collection<Object[]> paramsList) throws DbException {
        return this.getSession().batch(sql, paramsList);
    }
    
    @Override
    public int[] batch(final String[] sql) throws DbException {
        return this.getSession().batch(sql);
    }
    
    @Override
    public void begin() throws DbException {
        this.getSession().begin();
    }
    
    @Override
    public void close() {
        
        //rollback
        try {
            if (this.session != null && this.isTransactionStarted()) {
                this.rollback();
            }
        } catch (final Exception e) {
        }
        
        //release connection
        this.pool.add(this);
    }
    
    @Override
    public void closePoolResource() {
        try {
            if (this.session != null) {
                this.getSession().close();
            }
        } catch (final DbException e) {
        }
    }
    
    @Override
    public void commit() throws DbException {
        this.getSession().commit();
    }
    
    @Override
    public void execute(final DbQuery query) throws DbException {
        this.getSession().execute(query);
    }
    
    @Override
    public void execute(final String sql) throws DbException {
        this.getSession().execute(sql);
    }
    
    @Override
    public void execute(final String sql, final Object[] params) throws DbException {
        this.getSession().execute(sql, params);
    }
    
    protected DbConnectionCloseable getSession() throws DbException {
        
        if (this.pooled) {
            throw new DbException("Can't get session from resource in pool");
        }
        if (this.session == null) {
            this.session = this.sessionRepository.getConnection();
        }
        return this.session;
    }
    
    @Override
    public boolean isConnected() {
        try {
            return this.getSession().isConnected();
        } catch (final DbException e) {
            return false;
        }
    }
    
    @Override
    public boolean isPooled() {
        return this.pooled;
    }
    
    @Override
    public boolean isTransactionStarted() throws DbException {
        return this.getSession().isTransactionStarted();
    }
    
    @Override
    public void prepare() throws DbException {
        this.getSession().prepare();
    }
    
    @Override
    public void rollback() throws DbException {
        this.getSession().rollback();
    }
    
    @Override
    public DbRecordSet select(final DbQuery query) throws DbException {
        return this.getSession().select(query);
    }
    
    @Override
    public DbRecordSet select(final String sql) throws DbException {
        return this.getSession().select(sql);
    }
    
    @Override
    public DbRecordSet select(final String sql, final Object[] params) throws DbException {
        return this.getSession().select(sql, params);
    }
    
    @Override
    public void setPooled(final boolean pooled) {
        this.pooled = pooled;
    }
    
    @Override
    public int update(final DbQuery query) throws DbException {
        return this.getSession().update(query);
    }
    
    @Override
    public int update(final String sqlList) throws DbException {
        return this.getSession().update(sqlList);
    }

    @Override
    public int update(final String sql, final Object[] paramsList) throws DbException {
        return this.getSession().update(sql, paramsList);
    }
    
}
