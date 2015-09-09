package org.diablitozzz.jera.console.cli;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Properties;

public class CliParser {
    
    static private Object castParam(final String value, final Class<?> type) throws CliParseException {
        
        //byte[]
        if (CliUtilObject.isInstanceOf(type, byte[].class)) {
            return value.getBytes();
        }
        //enum
        else if (type.isEnum()) {
            //ищем по значению to string
            final Object[] items = type.getEnumConstants();
            for (final Object item : items) {
                if (item.toString().equals(value)) {
                    return item;
                }
            }
        }
        // int
        else if (CliUtilObject.isInstanceOf(type, int.class)) {
            return CliUtilObject.toInteger(value);
        }
        // Integer
        else if (CliUtilObject.isInstanceOf(type, Integer.class)) {
            return CliUtilObject.toIntegerObject(value);
        }
        // float
        else if (CliUtilObject.isInstanceOf(type, float.class)) {
            return CliUtilObject.toFloat(value);
        }
        // Float
        else if (CliUtilObject.isInstanceOf(type, Float.class)) {
            return CliUtilObject.toFloatObject(value);
        }
        // short
        else if (CliUtilObject.isInstanceOf(type, short.class)) {
            return CliUtilObject.toShort(value);
        }
        // Short
        else if (CliUtilObject.isInstanceOf(type, Short.class)) {
            return CliUtilObject.toShortObject(value);
        }
        // double
        else if (CliUtilObject.isInstanceOf(type, double.class)) {
            return CliUtilObject.toDouble(value);
        }
        // Double
        else if (CliUtilObject.isInstanceOf(type, Double.class)) {
            return CliUtilObject.toDoubleObject(value);
        }
        // boolean
        else if (CliUtilObject.isInstanceOf(type, boolean.class)) {
            return CliUtilObject.toBoolean(value);
        }
        // Boolean
        else if (CliUtilObject.isInstanceOf(type, Boolean.class)) {
            return CliUtilObject.toBooleanObject(value);
        }
        // long
        else if (CliUtilObject.isInstanceOf(type, long.class)) {
            return CliUtilObject.toLong(value);
        }
        // Long
        else if (CliUtilObject.isInstanceOf(type, Long.class)) {
            return CliUtilObject.toLongObject(value);
        }
        // String
        else if (CliUtilObject.isInstanceOf(type, String.class)) {
            return value;
        }
        // Date
        else if (CliUtilObject.isInstanceOf(type, java.util.Date.class)) {
            return CliUtilObject.toDate(value);
        }
        // Calendar
        else if (CliUtilObject.isInstanceOf(type, java.util.Calendar.class)) {
            final Calendar out = Calendar.getInstance();
            out.setTime(CliUtilObject.toDate(value));
            return out;
        }
        
        // Null
        else if (value == null) {
            return null;
        }
        //natural object
        else if (type.equals(Object.class)) {
            return value;
        }
        
        throw new CliParseException("Can't decode type: " + type.getCanonicalName() + " value: " + value);
    }
    
    static public void parse(final Object command, final String[] args) throws CliParseException {
        final Properties props = CliParser.parseProperties(args);
        
        for (final Field field : command.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(CliArgument.class)) {
                continue;
            }
            final CliArgument argument = field.getAnnotation(CliArgument.class);
            
            if (!props.containsKey(argument.name())) {
                
                if (argument.required()) {
                    throw new CliArgumentNotFoundException(argument);
                }
                continue;
            }
            
            try {
                field.setAccessible(true);
                final Object value = CliParser.castParam(props.getProperty(argument.name()).toString(), field.getType());
                field.set(command, value);
            } catch (final Exception e) {
                throw new CliArgumentCastException(argument, e);
            }
        }
        
    }
    
    static public Properties parseProperties(final String args[]) throws CliParseException {
        final StringBuilder sb = new StringBuilder();
        for (final String arg : args) {
            sb.append(arg);
            sb.append("\n");
        }
        final Properties props = new Properties();
        try {
            props.load(new StringReader(sb.toString()));
        } catch (final IOException e) {
            throw new CliParseException("Can't parse args", e);
        }
        return props;
    }
    
}
