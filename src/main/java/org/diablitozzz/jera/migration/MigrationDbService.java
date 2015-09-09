package org.diablitozzz.jera.migration;

import java.util.List;

public interface MigrationDbService {
    
    void down(Migration migration) throws MigrationException;
    
    void install() throws MigrationException;
    
    boolean isIntalled() throws MigrationException;
    
    List<Migration> list() throws MigrationException;
    
    void uninstall() throws MigrationException;
    
    void up(Migration migration) throws MigrationException;
}
