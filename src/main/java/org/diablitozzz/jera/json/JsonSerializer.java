package org.diablitozzz.jera.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class JsonSerializer {

    private static Method getToStringMethod(final Object object) {
        for (final Method method : object.getClass().getDeclaredMethods()) {
            if (method.getName().equals("toString") && method.getReturnType() == String.class && method.getParameterCount() == 0) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private static Object getValue(final Object object, final Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (final Throwable e) {
            return null;
        }
    }
    
    public static JsonObject serialize(final Object object) {
        if (object == null) {
            return null;
        }
        final Object result = JsonSerializer.serializeDo(object, 0, 100);
        if (result instanceof JsonObject) {
            return (JsonObject) result;
        } else {
            return JsonObject.create().set(object.getClass().getSimpleName(), result);
        }
    }
    
    private static Object serializeDo(final Object object, final int level, final int maxLevel) {
        if (object == null) {
            return null;
        }
        if (level > maxLevel) {
            return null;
        }
        if (object instanceof Iterable<?>) {
            final JsonObject data = new JsonObject();
            final Iterator<?> iterator = ((Iterable<?>) object).iterator();
            while (iterator.hasNext()) {
                data.add(JsonSerializer.serializeDo(iterator.next(), level + 1, maxLevel));
            }
            return data;
        } else if (object.getClass().isArray()) {
            final JsonObject data = new JsonObject();
            final int size = Array.getLength(object);
            for (int i = 0; i < size; i++) {
                data.add(JsonSerializer.serializeDo(Array.get(object, i), level + 1, maxLevel));
            }
            return data;
        } else if (object instanceof Number) {
            return object;
        } else if (object instanceof String) {
            return object;
        } else if (object instanceof Temporal) {
            return object;
        } else if (object instanceof Date) {
            return object;
        } else if (object instanceof Calendar) {
            return object;
        } else if (object instanceof Boolean) {
            return object;
        } else if (object.getClass() == int.class) {
            return object;
        } else if (object.getClass() == double.class) {
            return object;
        } else if (object.getClass() == float.class) {
            return object;
        } else if (object.getClass() == boolean.class) {
            return object;
        } else if (object.getClass() == long.class) {
            return object;
        } else if (object.getClass().isEnum()) {
            return ((Enum<?>) object).name();
        } else if (object.getClass().getSimpleName().contains("$Lambda$")) {
            return null;
        }
        final Method toStringMethod = JsonSerializer.getToStringMethod(object);
        if (toStringMethod != null) {
            try {
                return toStringMethod.invoke(object, new Object[] {});
            } catch (final Throwable e) {
                return e.toString();
            }
        }
        
        final JsonObject data = new JsonObject();
        for (final Field field : object.getClass().getDeclaredFields()) {
            final Object value = JsonSerializer.getValue(object, field);
            if (value == object) {
                continue;
            }
            data.set(field.getName(), JsonSerializer.serializeDo(value, level + 1, maxLevel));
        }
        return JsonObject.create().set(object.getClass().getSimpleName(), data);
    }

}
