package org.diablitozzz.jera.migration;

import java.util.List;

public interface MigrationRepository {

	Migration generateNewMigration() throws MigrationException;

	List<Migration> list() throws MigrationException;

}
