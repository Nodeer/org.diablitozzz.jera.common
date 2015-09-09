package org.diablitozzz.jera.db.platform;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import org.diablitozzz.jera.date.DateTime;

class DbPlatformUtil {
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    
    public static boolean inArray(final Object[] a, final Object key) {
        if (key == null) {
            return false;
        }
        for (final Object item : a) {
            if (key.equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean toBoolean(final Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).intValue() == 1 ? true : false;
        } else if (value instanceof Float) {
            return ((Float) value).intValue() == 1 ? true : false;
        } else if (value instanceof Short) {
            return ((Short) value).shortValue() == 1 ? true : false;
        } else if (value instanceof Double) {
            return ((Double) value).intValue() == 1 ? true : false;
        } else if (value instanceof Long) {
            return ((Long) value).intValue() == 1 ? true : false;
        } else if (value instanceof String) {
            if (((String) value).equals("true")) {
                return true;
            } else if (((String) value).equals("1")) {
                return true;
            }
            return false;
        }
        return false;
    }
    
    public static Boolean toBooleanObject(final Object value) {
        if (value == null) {
            return null;
        }
        return Boolean.valueOf(DbPlatformUtil.toBoolean(value));
    }
    
    public static Calendar toCalendar(final Object value) {
        return DbPlatformUtil.toCalendar(value, DbPlatformUtil.simpleDateFormat);
        
    }
    
    public static Calendar toCalendar(final Object value, final DateFormat dateFormat) {
        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        
        final java.util.Date date = DbPlatformUtil.toDate(value, dateFormat);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
    public static java.util.Date toDate(final Object value) {
        return DbPlatformUtil.toDate(value, DbPlatformUtil.simpleDateFormat);
    }
    
    public static java.util.Date toDate(final Object value, final DateFormat dateFormat) {
        if (value == null) {
            return null;
        } else if (value instanceof java.util.Date) {
            return (java.util.Date) value;
        } else if (value instanceof java.sql.Date) {
            return new java.util.Date(((java.sql.Date) value).getTime());
        } else if (value instanceof java.sql.Timestamp) {
            return new java.util.Date(((java.sql.Timestamp) value).getTime());
        } else if (value instanceof Integer) {
            return new java.util.Date(((Integer) value).longValue());
        } else if (value instanceof Float) {
            return new java.util.Date(((Float) value).longValue());
        } else if (value instanceof Short) {
            return new java.util.Date(((Short) value).longValue());
        } else if (value instanceof Double) {
            return new java.util.Date(((Double) value).longValue());
        } else if (value instanceof Boolean) {
            return null;
        } else if (value instanceof Long) {
            return new java.util.Date(((Long) value).longValue());
        } else if (value instanceof String) {
            try {
                return dateFormat.parse((String) value);
            } catch (final ParseException e) {
                return null;
            }
        }
        return null;
    }
    
    public static double toDouble(final Object value) {
        if (value instanceof Double) {
            return ((Double) value).doubleValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Float) {
            return ((Float) value).doubleValue();
        } else if (value instanceof Short) {
            return ((Short) value).doubleValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1f : 0f;
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf(((String) value)).doubleValue();
        } else if (value instanceof java.util.Date) {
            Long.valueOf(((java.util.Date) value).getTime()).doubleValue();
        }
        return 0d;
    }
    
    public static Double toDoubleObject(final Object value) {
        if (value == null) {
            return null;
        }
        return Double.valueOf(DbPlatformUtil.toDouble(value));
    }
    
    public static <T extends Enum<?>> T toEnum(final Object value, final Class<T> enumClass) {
        final String sValue = DbPlatformUtil.toString(value);
        final T[] items = enumClass.getEnumConstants();
        for (final T item : items) {
            if (item.name().equals(sValue)) {
                return item;
            }
        }
        return null;
    }
    
    public static float toFloat(final Object value) {
        if (value instanceof Float) {
            return ((Float) value).floatValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).floatValue();
        } else if (value instanceof Short) {
            return ((Short) value).floatValue();
        } else if (value instanceof Double) {
            return ((Double) value).floatValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1f : 0f;
        } else if (value instanceof Long) {
            return ((Long) value).floatValue();
        } else if (value instanceof String) {
            return Float.valueOf(((String) value)).floatValue();
        } else if (value instanceof java.util.Date) {
            Long.valueOf(((java.util.Date) value).getTime()).floatValue();
        }
        
        return 0f;
    }
    
    public static Float toFloatObject(final Object value) {
        if (value == null) {
            return null;
        }
        return Float.valueOf(DbPlatformUtil.toFloat(value));
        
    }
    
    public static int toInteger(final Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        } else if (value instanceof Float) {
            return ((Float) value).intValue();
        } else if (value instanceof Short) {
            return ((Short) value).intValue();
        } else if (value instanceof Double) {
            return ((Double) value).intValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf(((String) value)).intValue();
        } else if (value instanceof java.util.Date) {
            Long.valueOf(((java.util.Date) value).getTime()).intValue();
        }
        return 0;
    }
    
    public static Integer toIntegerObject(final Object value) {
        if (value == null) {
            return null;
        }
        return Integer.valueOf(DbPlatformUtil.toInteger(value));
        
    }
    
    public static LocalDate toLocalDate(final long timestamp) {
        return DateTime.toLocalDateTime(timestamp).toLocalDate();
    }
    
    public static LocalDateTime toLocalDateTime(final long timestamp) {
        final LocalDateTime out = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return out;
    }
    
    public static long toLong(final Object value) {
        if (value instanceof Long) {
            return ((Long) value).longValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof Float) {
            return ((Float) value).longValue();
        } else if (value instanceof Short) {
            return ((Short) value).longValue();
        } else if (value instanceof Double) {
            return ((Double) value).longValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        } else if (value instanceof String) {
            return Long.valueOf(((String) value)).longValue();
        } else if (value instanceof java.util.Date) {
            ((java.util.Date) value).getTime();
        }
        return 0L;
    }

    public static Long toLongObject(final Object value) {
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            return null;
        }
        return Long.valueOf(DbPlatformUtil.toLong(value));
    }
    
    public static Long[] toLongObjectArray(final Object elements) {
        
        if (elements == null) {
            return null;
        }
        if (elements instanceof Long[]) {
            return (Long[]) elements;
        }
        if (elements instanceof Collection) {
            final Collection<?> cols = ((Collection<?>) elements);
            final Long[] out = new Long[cols.size()];
            int i = 0;
            for (final Object col : cols) {
                out[i] = DbPlatformUtil.toLongObject(col);
                i++;
            }
            return out;
        }
        if (elements.getClass().isArray()) {
            final int size = Array.getLength(elements);
            final Long[] out = new Long[size];
            for (int i = 0; i < size; i++) {
                out[i] = DbPlatformUtil.toLongObject(Array.get(elements, i));
            }
            return out;
        }
        return null;
    }
    
    public static Object[] toObjectArray(final Object elements) {
        if (elements == null) {
            return null;
        }
        
        if (elements instanceof Object[]) {
            return (Object[]) elements;
        }
        
        if (elements instanceof Collection) {
            return ((Collection<?>) elements).toArray();
        }
        
        if (elements.getClass().isArray()) {
            final int size = Array.getLength(elements);
            final Object[] data = new Object[size];
            for (int i = 0; i < size; i++) {
                data[i] = Array.get(elements, i);
            }
            return data;
        }
        return null;
    }
    
    public static short toShort(final Object value) {
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).shortValue();
        } else if (value instanceof Float) {
            return ((Float) value).shortValue();
        } else if (value instanceof Double) {
            return ((Double) value).shortValue();
        } else if (value instanceof Boolean) {
            return (short) (((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Long) {
            return ((Long) value).shortValue();
        } else if (value instanceof String) {
            return Short.valueOf(((String) value)).shortValue();
        } else if (value instanceof java.util.Date) {
            Long.valueOf(((java.util.Date) value).getTime()).shortValue();
        }
        return 0;
    }
    
    public static Short toShortObject(final Object value) {
        if (value == null) {
            return null;
        }
        return Short.valueOf(DbPlatformUtil.toShort(value));
    }
    
    public static String toString(final Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    public static long toTimeStamp(final LocalDate localDate) {
        final LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);
        return DateTime.toTimeStamp(localDateTime);
    }
    
    public static long toTimeStamp(final LocalDateTime localDateTime) {
        final ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return DateTime.toTimeStamp(zdt);
    }
    
    public static long toTimeStamp(final ZonedDateTime zdt) {
        return zdt.toInstant().toEpochMilli();
    }

    public static UUID toUUID(final Object value) {
        if (value instanceof UUID) {
            return (UUID) value;
        }
        if (value instanceof String) {
            return UUID.fromString((String) value);
        }
        return UUID.fromString(value.toString());
    }

    public static UUID[] toUUIDArray(final Object elements) {
        if (elements == null) {
            return null;
        }
        
        if (elements instanceof UUID[]) {
            return (UUID[]) elements;
        }
        
        if (elements instanceof Collection) {
            final int size = ((Collection<?>) elements).size();
            return ((Collection<?>) elements).toArray(new UUID[size]);
        }
        
        if (elements.getClass().isArray()) {
            final int size = Array.getLength(elements);
            final UUID[] data = new UUID[size];
            for (int i = 0; i < size; i++) {
                data[i] = (UUID) Array.get(elements, i);
            }
            return data;
        }
        return null;
    }
    
    public static ZonedDateTime toZonedDateTime(final long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault());
    }
    
}
