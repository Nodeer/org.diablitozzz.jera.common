package org.diablitozzz.jera.db;

import java.util.List;

public interface DbRecordSet extends AutoCloseable, Iterable<DbRecord> {
    
    @Override
    public void close();
    
    public String[] getColumns();
    
    public DbRecord getFirstAndClose();
    
    public List<DbRecord> toListAndClose();
}
