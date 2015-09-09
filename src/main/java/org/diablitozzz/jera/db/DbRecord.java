package org.diablitozzz.jera.db;

public interface DbRecord {

    public boolean containsColumn(String columnName);

    public <T> T get(int columnIndex, Class<T> classValue);

    public <T> T get(String columnName, Class<T> classValue);

    public String[] getColumns();
}
