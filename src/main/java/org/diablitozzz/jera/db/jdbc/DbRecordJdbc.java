package org.diablitozzz.jera.db.jdbc;

import org.diablitozzz.jera.db.DbRecord;
import org.diablitozzz.jera.db.platform.DbPlatform;

class DbRecordJdbc implements DbRecord {

    private final Object[] vals;
    private final DbRecordSetMetaDataJdbc metaData;
    private final DbPlatform platform;
    
    public DbRecordJdbc(final Object[] vals, final DbRecordSetMetaDataJdbc metaData, final DbPlatform platform) {
        this.vals = vals;
        this.metaData = metaData;
        this.platform = platform;
    }

    @Override
    public boolean containsColumn(final String columnName) {
        return this.metaData.containsColumn(columnName);
    }

    @Override
    public <T> T get(final int columnIndex, final Class<T> classValue) {
        final Object val = this.vals[columnIndex];
        return this.platform.fromDb(val, classValue);
    }

    @Override
    public <T> T get(final String columnName, final Class<T> classValue) {
        final int columnIndex = this.metaData.getColumnIndex(columnName);
        return this.get(columnIndex, classValue);
    }

    @Override
    public String[] getColumns() {
        return this.metaData.getColumns();
    }

}
