package org.diablitozzz.jera.db.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.DbRecord;
import org.diablitozzz.jera.db.DbRecordSet;
import org.diablitozzz.jera.db.platform.DbPlatform;

public class DbRecordSetJdbc implements DbRecordSet {

    private final DbRecordSetMetaDataJdbc metaData;
    private DbRecordSetIteratorJdbc iterator;

    public DbRecordSetJdbc(final ResultSet resultSet, final DbPlatform platform) throws DbException {
        try {
            this.metaData = new DbRecordSetMetaDataJdbc(resultSet.getMetaData());
            this.iterator = new DbRecordSetIteratorJdbc(resultSet, this.metaData, platform);
        } catch (final SQLException e) {
            throw new DbException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void close() {
        this.iterator.close();
    }

    @Override
    public String[] getColumns() {
        return this.metaData.getColumns();
    }

    @Override
    public DbRecord getFirstAndClose() {
        final DbRecord record = this.iterator.next();
        this.close();
        return record;
    }

    @Override
    public Iterator<DbRecord> iterator() {
        return this.iterator;
    }

    @Override
    public List<DbRecord> toListAndClose() {
        final List<DbRecord> out = new ArrayList<>();
        for (final DbRecord record : this) {
            out.add(record);
        }
        this.close();
        return out;
    }

}
