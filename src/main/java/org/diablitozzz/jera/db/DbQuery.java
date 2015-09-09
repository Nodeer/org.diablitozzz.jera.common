package org.diablitozzz.jera.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DbQuery {
    
    public static DbQuery createInsert(final Map<String, Object> map, final String table) {
        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(table);
        return DbQuery.createInsert(sql, map);
    }
    
    public static DbQuery createInsert(final String prefix, final Map<String, Object> map) {
        final StringBuilder sql = new StringBuilder();
        sql.append(prefix);
        return DbQuery.createInsert(sql, map);
    }
    
    private static DbQuery createInsert(final StringBuilder sql, final Map<String, Object> map) {
        final List<Object> params = new ArrayList<>(map.size());
        sql.append("(");

        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> entry = iterator.next();
            sql.append(entry.getKey());
            if (iterator.hasNext()) {
                sql.append(", ");
            }
            params.add(entry.getValue());
        }
        
        sql.append(") VALUES(");
        iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            sql.append("?");
            iterator.next();
            if (iterator.hasNext()) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return new DbQuery(sql.toString(), params.toArray());
    }

    public static DbQuery createUpdate(final Map<String, Object> map, final String table, final String where, final Object[] whereParams) {
        final StringBuilder sql = new StringBuilder();
        final List<Object> params = new ArrayList<>(map.size());
        sql.append("UPDATE ");
        sql.append(table);
        sql.append(" SET ");
        final Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> entry = iterator.next();
            sql.append(entry.getKey());
            sql.append("=?");
            if (iterator.hasNext()) {
                sql.append(", ");
            }
            params.add(entry.getValue());
        }
        sql.append(" WHERE ");
        sql.append(where);
        if (whereParams != null) {
            for (final Object whereParam : whereParams) {
                params.add(whereParam);
            }
        }
        return new DbQuery(sql.toString(), params.toArray());
    }
    
    private final String sql;
    
    private final Object[] params;
    
    public DbQuery(final String sql, final Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    public Object[] getParams() {
        return this.params;
    }

    public String getSql() {
        return this.sql;
    }

}
