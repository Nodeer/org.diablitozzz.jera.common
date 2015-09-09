package org.diablitozzz.jera.db.platform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class DbPlatformSqlite implements DbPlatform {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromDb(final Object value, final Class<T> classValue) {
        if (value == null) {
            return null;
        }
        if (classValue == String.class) {
            return (T) DbPlatformUtil.toString(value);
        } else if (classValue == Boolean.class) {
            return (T) Boolean.valueOf(DbPlatformUtil.toLong(value) == 1);
        } else if (classValue == Long.class) {
            return (T) DbPlatformUtil.toLongObject(value);
        } else if (classValue == Integer.class) {
            return (T) DbPlatformUtil.toIntegerObject(value);
        } else if (classValue == Float.class) {
            return (T) DbPlatformUtil.toFloatObject(value);
        } else if (classValue == Double.class) {
            return (T) DbPlatformUtil.toDoubleObject(value);
        } else if (classValue == LocalDate.class) {
            return (T) DbPlatformUtil.toLocalDate(DbPlatformUtil.toLong(value));
        } else if (classValue == LocalDateTime.class) {
            return (T) DbPlatformUtil.toLocalDateTime(DbPlatformUtil.toLong(value));
        } else if (classValue == ZonedDateTime.class) {
            return (T) DbPlatformUtil.toZonedDateTime(DbPlatformUtil.toLong(value));
        } else if (classValue.isEnum()) {
            return (T) DbPlatformUtil.toEnum(value, (Class<Enum<?>>) classValue);
        } else if (classValue == Date.class) {
            return (T) new Date(DbPlatformUtil.toLong(value));
        }
        return (T) value;
    }

    @Override
    public Object toDb(final Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        } else if (value instanceof LocalDateTime) {
            return DbPlatformUtil.toTimeStamp((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return DbPlatformUtil.toTimeStamp((LocalDate) value);
        } else if (value instanceof ZonedDateTime) {
            return DbPlatformUtil.toTimeStamp((ZonedDateTime) value);
        } else if (value.getClass().isEnum()) {
            return ((Enum<?>) value).name();
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        return value;
    }

}
