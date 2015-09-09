package org.diablitozzz.jera.migration;

public abstract class MigrationBasic implements Migration {
    
    private final MigrationId id;
    private boolean installed;
    
    public MigrationBasic(final String id) {
        try {
            this.id = MigrationId.createFromString(id);
        } catch (final MigrationException e) {
            throw new RuntimeException(e);
        }
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
    
}
