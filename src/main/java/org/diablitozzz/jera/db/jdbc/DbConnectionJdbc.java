package org.diablitozzz.jera.db.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.diablitozzz.jera.db.DbArray;
import org.diablitozzz.jera.db.DbConnectionCloseable;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.DbQuery;
import org.diablitozzz.jera.db.DbRecordSet;
import org.diablitozzz.jera.db.log.DbLogService;
import org.diablitozzz.jera.db.platform.DbPlatform;

public class DbConnectionJdbc implements DbConnectionCloseable {
    
    private final DbResourceJdbcAbstract connection;
    private final DbLogService logService;
    private final DbPlatform platform;
    
    public DbConnectionJdbc(final DbResourceJdbcAbstract connection, final DbLogService logService, final DbPlatform platform) {
        this.connection = connection;
        this.logService = logService;
        this.platform = platform;
    }
    
    protected void applyStatementParams(final PreparedStatement statement, final Object[] params) throws Throwable {
        
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof DbArray) {
                final DbArray param = (DbArray) params[i];
                statement.setArray(i + 1, statement.getConnection().createArrayOf(param.getType(), param.getElements()));
            } else if (params[i] instanceof Enum) {
                statement.setObject(i + 1, this.platform.toDb(params[i]), java.sql.Types.OTHER);
            } else {
                statement.setObject(i + 1, this.platform.toDb(params[i]));
            }
        }
    }
    
    @Override
    public int[] batch(final String sql, final Collection<Object[]> paramsList) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        
        try (PreparedStatement statement = this.connection.getConnection().prepareStatement(sql)) {
            for (final Object[] params : paramsList) {
                this.applyStatementParams(statement, params);
                statement.addBatch();
            }
            return statement.executeBatch();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e.getLocalizedMessage(), e);
        } finally {
            this.logService.log(start, "BATCH {}", new Object[] { sql, paramsList }, error);
        }
    }
    
    @Override
    public int[] batch(final String[] sqlList) throws DbException {
        
        final long start = System.nanoTime();
        final Throwable error = null;
        
        try (Statement statement = this.connection.getConnection().createStatement()) {
            for (final String sql : sqlList) {
                statement.addBatch(sql);
            }
            return statement.executeBatch();
            
        } catch (final Throwable e) {
            throw new DbException(e);
        } finally {
            this.logService.log(start, "BATCH {}", new Object[] { sqlList }, error);
        }
    }
    
    @Override
    public void begin() throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try {
            this.connection.begin();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "BEGIN", new Object[] {}, error);
        }
    }
    
    @Override
    public void close() {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try {
            this.connection.close();
        } catch (final Throwable e) {
            error = e;
        } finally {
            this.logService.log(start, "CLOSE", new Object[] {}, error);
        }
    }
    
    private void closeStatement(final Statement closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (final Throwable e) {
            }
        }
    }
    
    @Override
    public void commit() throws DbException {
        final long start = System.nanoTime();
        Throwable error = null;
        try {
            this.connection.commit();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "COMMIT", new Object[] {}, error);
        }
    }
    
    @Override
    public void execute(final DbQuery query) throws DbException {
        this.execute(query.getSql(), query.getParams());
    }
    
    @Override
    public void execute(final String sql) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        
        try (Statement statement = this.connection.getConnection().createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            error = e;
            throw new DbException(e.getLocalizedMessage(), e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql }, error);
        }
    }
    
    @Override
    public void execute(final String sql, final Object[] params) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        
        try (PreparedStatement statement = this.connection.getConnection().prepareStatement(sql)) {
            this.applyStatementParams(statement, params);
            statement.execute();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e.getLocalizedMessage(), e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql, params }, error);
        }
    }
    
    public String getUrl() {
        return this.connection.getURL();
    }
    
    @Override
    public boolean isConnected() {
        if (this.connection == null) {
            return false;
        }
        try {
            return !this.connection.getConnection().isClosed();
        } catch (final Throwable e) {
            return false;
        }
    }
    
    @Override
    public boolean isTransactionStarted() {
        return this.connection.isTransactionStarted();
    }
    
    @Override
    public void prepare() throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try {
            this.connection.prepare();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "PREPARE", new Object[] {}, error);
        }
    }
    
    @Override
    public void rollback() throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try {
            this.connection.rollback();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "ROLLBACK", new Object[] {}, error);
        }
    }
    
    @Override
    public DbRecordSet select(final DbQuery query) throws DbException {
        return this.select(query.getSql(), query.getParams());
    }
    
    @Override
    public DbRecordSet select(final String sql) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        Statement statement = null;
        try {
            statement = this.connection.getConnection().createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);
            return new DbRecordSetJdbc(resultSet, this.platform);
            
        } catch (final Throwable e) {
            error = e;
            this.closeStatement(statement);
            throw new DbException(e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql }, error);
        }
    }
    
    @Override
    public DbRecordSet select(final String sql, final Object[] params) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        PreparedStatement statement = null;
        try {
            
            statement = this.connection.getConnection().prepareStatement(sql);
            this.applyStatementParams(statement, params);
            final ResultSet resultSet = statement.executeQuery();
            return new DbRecordSetJdbc(resultSet, this.platform);
            
        } catch (final Throwable e) {
            error = e;
            this.closeStatement(statement);
            throw new DbException(e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql, params }, error);
        }
    }
    
    @Override
    public int update(final DbQuery query) throws DbException {
        return this.update(query.getSql(), query.getParams());
    }
    
    @Override
    public int update(final String sql) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try (Statement statement = this.connection.getConnection().createStatement()) {
            return statement.executeUpdate(sql);
            
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql }, error);
        }
    }

    @Override
    public int update(final String sql, final Object[] params) throws DbException {
        
        final long start = System.nanoTime();
        Throwable error = null;
        try (PreparedStatement statement = this.connection.getConnection().prepareStatement(sql)) {
            this.applyStatementParams(statement, params);
            return statement.executeUpdate();
        } catch (final Throwable e) {
            error = e;
            throw new DbException(e);
        } finally {
            this.logService.log(start, "{}", new Object[] { sql, params }, error);
        }
    }
    
}
