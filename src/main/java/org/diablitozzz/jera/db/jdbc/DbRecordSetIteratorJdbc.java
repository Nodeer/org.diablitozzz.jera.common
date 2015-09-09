package org.diablitozzz.jera.db.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.diablitozzz.jera.db.DbRecord;
import org.diablitozzz.jera.db.platform.DbPlatform;

class DbRecordSetIteratorJdbc implements Iterator<DbRecord>, AutoCloseable {
    
    private final ResultSet resultSet;
    private final DbRecordSetMetaDataJdbc metaData;
    private DbRecord current;
    private boolean closed;
    private boolean nexted = false;
    private final DbPlatform platform;
    
    public DbRecordSetIteratorJdbc(final ResultSet resultSet, final DbRecordSetMetaDataJdbc metaData, final DbPlatform platform) {
        this.resultSet = resultSet;
        this.metaData = metaData;
        this.current = null;
        this.closed = false;
        this.platform = platform;
    }
    
    private void assertClosed() {
        
        if (this.closed) {
            throw new IllegalStateException("Result is closed");
        }
    }
    
    @Override
    public void close() {
        
        this.closed = true;
        try (Statement statement = this.resultSet.getStatement()) {
            this.resultSet.close();
            statement.close();
        } catch (final SQLException e) {
        }
    }
    
    private DbRecord createRecord() {
        
        final int colCount = this.metaData.getColCount();
        final Object[] vals = new Object[colCount];
        for (int i = 0; i < colCount; i++) {
            try {
                vals[i] = this.resultSet.getObject(i + 1);
            } catch (final SQLException e) {
                throw new IllegalStateException("Can't read record ", e);
            }
        }
        return new DbRecordJdbc(vals, this.metaData, this.platform);
    }
    
    @Override
    public boolean hasNext() {
        this.nexted = true;
        if (this.closed) {
            return false;
        }
        if (this.current != null) {
            return true;
        }
        try {
            final boolean hasNext = this.resultSet.next();
            if (!hasNext) {
                this.close();
                return false;
            }
            //load current
            this.current = this.createRecord();
            return true;
        } catch (final SQLException e) {
            this.close();
            throw new IllegalStateException("Can't get next record in result", e);
        }
    }
    
    public boolean isEmpty() {
        this.assertClosed();
        return !this.hasNext();
    }
    
    @Override
    public DbRecord next() {
        if (!this.nexted) {
            this.hasNext();
            this.nexted = false;
        }
        final DbRecord current = this.current;
        this.current = null;
        return current;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation not support");
    }
    
}
