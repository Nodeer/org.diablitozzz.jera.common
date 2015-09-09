package org.diablitozzz.jera.db;

import java.util.Collection;

public interface DbConnection {
    
    int[] batch(String sql, Collection<Object[]> paramsList) throws DbException;

    int[] batch(String[] sql) throws DbException;
    
    void begin() throws DbException;
    
    void commit() throws DbException;
    
    void execute(DbQuery query) throws DbException;
    
    void execute(String sql) throws DbException;
    
    void execute(String sql, Object[] params) throws DbException;
    
    boolean isConnected();
    
    boolean isTransactionStarted() throws DbException;
    
    void prepare() throws DbException;
    
    void rollback() throws DbException;
    
    DbRecordSet select(DbQuery query) throws DbException;
    
    DbRecordSet select(String sql) throws DbException;
    
    DbRecordSet select(String sql, Object[] params) throws DbException;
    
    int update(DbQuery query) throws DbException;
    
    int update(String sqlList) throws DbException;
    
    int update(String sql, Object[] paramsList) throws DbException;
    
}
