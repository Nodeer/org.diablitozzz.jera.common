package org.diablitozzz.jera.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MigrationService {

	public interface Handler {

		void afterDown(Migration migration);

		void afterUp(Migration migration);

		void beforeDown(Migration migration);

		void beforeUp(Migration migration);

	}

	private final MigrationDbService dbService;
	private final MigrationRepository repository;

	public MigrationService(final MigrationDbService dbService, final MigrationRepository repository) {
		this.dbService = dbService;
		this.repository = repository;
	}

	public Migration generateNewMigration() throws MigrationException {
		return this.repository.generateNewMigration();
	}

	public void install() throws MigrationException {
		this.dbService.install();
	}

	public boolean isInstalled() throws MigrationException {
		return this.dbService.isIntalled();
	}

	public List<Migration> list() throws MigrationException {

		final Map<MigrationId, Migration> migrationMap = new HashMap<>();
		//load from repository
		for (final Migration migration : this.repository.list()) {
			migrationMap.put(migration.getId(), migration);
			migration.setInstalled(false);
		}
		//load from db
		for (final Migration migration : this.dbService.list()) {
			if (migrationMap.containsKey(migration.getId())) {
				migrationMap.get(migration.getId()).setInstalled(true);
			} else {
				migration.setInstalled(true);
				migrationMap.put(migration.getId(), migration);
			}
		}
		//sort
		final List<Migration> out = new ArrayList<>(migrationMap.values());
		Collections.sort(out, Migration.ORDER_ASC);
		//return
		return Collections.unmodifiableList(out);
	}

	public void migrateToVersion(final MigrationId toVersion) throws MigrationException {
		this.migrateToVersion(toVersion, null);
	}

	public void migrateToVersion(final MigrationId toVersion, final Handler handler) throws MigrationException {

		//make up and down
		final List<Migration> forUp = new ArrayList<>();
		final List<Migration> forDown = new ArrayList<>();
		for (final Migration model : this.list()) {
			//если дата модели меньше равна версии то поднимаем, если больше откатываем
			if (model.getId().compareTo(toVersion) <= 0) {
				forUp.add(model);
			} else {
				forDown.add(model);
			}
		}
		//down
		Collections.sort(forDown, Migration.ORDER_DESC);
		for (final Migration migration : forDown) {
			if (!migration.isInstalled()) {
				continue;
			}
			if (handler != null) {
				handler.beforeDown(migration);
			}
			this.dbService.down(migration);
			if (handler != null) {
				handler.afterDown(migration);
			}
		}
		//up
		Collections.sort(forUp, Migration.ORDER_ASC);
		for (final Migration migration : forUp) {
			if (migration.isInstalled()) {
				continue;
			}
			if (handler != null) {
				handler.beforeUp(migration);
			}
			this.dbService.up(migration);
			if (handler != null) {
				handler.afterUp(migration);
			}
		}
	}

	public void uninstall() throws MigrationException {
		this.dbService.uninstall();
	}
}
