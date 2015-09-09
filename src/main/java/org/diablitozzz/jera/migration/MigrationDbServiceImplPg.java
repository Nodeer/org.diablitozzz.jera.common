package org.diablitozzz.jera.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.diablitozzz.jera.db.DbConnection;
import org.diablitozzz.jera.db.DbConnectionCloseable;
import org.diablitozzz.jera.db.DbConnectionRepository;
import org.diablitozzz.jera.db.DbException;
import org.diablitozzz.jera.db.DbRecord;
import org.diablitozzz.jera.db.DbRecordSet;

public class MigrationDbServiceImplPg implements MigrationDbService {
    
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
    private final DbConnectionRepository db;
    
    public MigrationDbServiceImplPg(final String table, final DbConnectionRepository db) {
        this.table = table;
        this.db = db;
    }
    
    @Override
    public void down(final Migration migration) throws MigrationException {
        
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            migration.down(connection);
            final String sql = "DELETE FROM " + this.table + " WHERE version=?";
            this.execute(sql, new Object[] { migration.getId().toTime() });
        } catch (final Exception e) {
            throw new MigrationException(e);
        }
    }
    
    private void execute(final String sql) throws MigrationException {
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            connection.execute(sql);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    private void execute(final String sql, final Object[] params) throws MigrationException {
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            connection.execute(sql, params);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public void install() throws MigrationException {
        final String sql = "" +
                " CREATE TABLE " + this.table +
                " (" +
                " 		version BIGINT NOT NULL," +
                " 	    description text NOT NULL," +
                " 		CONSTRAINT pkey PRIMARY KEY (version) " +
                " )";
        this.execute(sql);
    }
    
    @Override
    public boolean isIntalled() throws MigrationException {
        //split schema and table
        String table;
        String schema;
        final String[] items = this.table.split("\\.");
        if (items.length == 2) {
            table = items[1];
            schema = items[0];
        } else {
            table = items[0];
            schema = "public";
        }
        final String sql = "SELECT EXISTS(SELECT 1 FROM pg_tables WHERE schemaname=? AND tablename=? LIMIT 1)";
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            return connection.select(sql, new Object[] { schema, table }).getFirstAndClose().get(0, Boolean.class);
        } catch (final DbException e) {
            throw new MigrationException(e);
        }
    }
    
    @Override
    public List<Migration> list() throws MigrationException {
        
        final String sql = "SELECT * FROM " + this.table;
        final List<Migration> out = new ArrayList<>();
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            try (DbRecordSet records = connection.select(sql)) {
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
        final String sql = "DROP TABLE IF EXISTS " + this.table + " CASCADE";
        this.execute(sql);
    }
    
    @Override
    public void up(final Migration migration) throws MigrationException {
        
        try (DbConnectionCloseable connection = this.db.getConnection()) {
            migration.up(connection);
            final String sql = "INSERT INTO " + this.table + "(version,description) VALUES(?,?)";
            this.execute(sql, new Object[] { migration.getId().toTime(), migration.getDescription() });
            
        } catch (final Exception e) {
            throw new MigrationException(e);
        }
    }
    
}
