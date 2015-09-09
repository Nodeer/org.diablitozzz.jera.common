package org.diablitozzz.jera.migration;

import java.util.Comparator;

import org.diablitozzz.jera.db.DbConnection;

public interface Migration {

	static final Comparator<Migration> ORDER_ASC = new Comparator<Migration>() {
		@Override
		public int compare(final Migration migrationA, final Migration migrationB) {
			return migrationA.getId().compareTo(migrationB.getId());
		}
	};

	static final Comparator<Migration> ORDER_DESC = new Comparator<Migration>() {
		@Override
		public int compare(final Migration migrationA, final Migration migrationB) {
			return migrationB.getId().compareTo(migrationA.getId());
		}
	};

	void down(DbConnection connection) throws Exception;

	String getDescription();

	MigrationId getId();

	boolean isInstalled();

	void setInstalled(boolean installed);

	void up(DbConnection connection) throws Exception;
}
