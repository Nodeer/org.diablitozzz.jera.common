package org.diablitozzz.jera.db.platform;

public interface DbPlatform {
    
    <T> T fromDb(Object value, Class<T> classValue);

    Object toDb(Object value);

}
