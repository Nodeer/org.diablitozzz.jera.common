package org.diablitozzz.jera.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.diablitozzz.jera.db.DbConnection;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.DbRecord;
import org.diablitozzz.jera.db.DbRecordSet;

public class MigrationDbServiceImplSqlite implements MigrationDbService {
    
    private static class MigrationImpl implements Migration {
        
        private final String description;
        private final MigrationId id;
        private boolean installed;
        
        public MigrationImpl(final MigrationId id, final String description) {
            this.id = id;
            this.description = description;
            this.installed = true;
        }
        
        @Override
        public void down(final DbConnection connection) throws MigrationException {
        }
        
        @Override
        public String getDescription() {
            return this.description;
        }
        
        @Override
        public MigrationId getId() {
            return this.id;
        }
        
        @Override
        public boolean isInstalled() {
            return this.installed;
        }
        
        @Override
        public void setInstalled(final boolean installed) {
            this.installed = installed;
        }
        
        @Override
        public void up(final DbConnection connection) throws MigrationException {
        }
        
    }
    
    private final String table;
    private final DbConnection connection;
    
    public MigrationDbServiceImplSqlite(final String table, final DbConnection connection) {
        this.table = table;
        this.connection = connection;
    }
    
    @Override
    public void down(final Migration migration) throws MigrationException {
        
        try {
            migration.down(this.connection);
            final String sql = "DELETE FROM " + this.table + " WHERE version=?";
            this.connection.execute(sql, new Object[] { migration.getId().toTime() });
        } catch (final Exception e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public void install() throws MigrationException {
        
        try {
            final String sql = "" +
                    " CREATE TABLE " + this.table +
                    " (" +
                    " 		version INTEGER NOT NULL," +
                    " 	    description TEXT NOT NULL," +
                    " 		CONSTRAINT pkey PRIMARY KEY (version) " +
                    " );";
            this.connection.execute(sql);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public boolean isIntalled() throws MigrationException {
        try {
            final String sql = "SELECT EXISTS(SELECT 1 FROM sqlite_master WHERE type='table' AND name=? LIMIT 1);";
            return this.connection
                    .select(sql, new Object[] { this.table })
                    .getFirstAndClose()
                    .get(0, Boolean.class);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public List<Migration> list() throws MigrationException {
        
        final String sql = "SELECT * FROM " + this.table;
        final List<Migration> out = new ArrayList<>();
        try {
            try (DbRecordSet records = this.connection.select(sql)) {
                for (final DbRecord record : records) {
                    final MigrationId id = MigrationId.createFromDate(record.get("version", Date.class));
                    final Migration migration = new MigrationImpl(id, record.get("description", String.class));
                    out.add(migration);
                }
            }
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
        return out;
    }
    
    @Override
    public void uninstall() throws MigrationException {
        try {
            final String sql = "DROP TABLE IF EXISTS " + this.table + ";";
            this.connection.execute(sql);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public void up(final Migration migration) throws MigrationException {
        
        try {
            migration.up(this.connection);
            final String sql = "INSERT INTO " + this.table + "(version,description) VALUES(?,?)";
            this.connection.execute(sql, new Object[] { migration.getId().toTime(), migration.getDescription() });
        } catch (final Exception e) {
            throw new MigrationException(e);
        }
    }
    
}
