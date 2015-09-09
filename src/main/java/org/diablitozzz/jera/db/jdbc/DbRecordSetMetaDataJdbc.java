package org.diablitozzz.jera.db.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class DbRecordSetMetaDataJdbc {

	private final String[] columns;
	private final Map<String, Integer> columnMap;

	public DbRecordSetMetaDataJdbc(final ResultSetMetaData metaData) throws SQLException {

		final int columnCount = metaData.getColumnCount();
		this.columns = new String[columnCount];
		this.columnMap = new HashMap<>(columnCount);

		for (int i = 0; i < columnCount; i++) {

			this.columns[i] = metaData.getColumnName(i + 1);
			this.columnMap.put(this.columns[i], i);
		}
	}

	public boolean containsColumn(final String name) {
		return this.columnMap.containsKey(name);
	}

	public int getColCount() {
		return this.columns.length;
	}

	public int getColumnIndex(final String name) {

		final Integer index = this.columnMap.get(name);
		if (index == null) {
			throw new IllegalArgumentException("Collumn: " + name + " not exists");
		}
		return index.intValue();
	}

	public String[] getColumns() {
		return this.columns;
	}
}
