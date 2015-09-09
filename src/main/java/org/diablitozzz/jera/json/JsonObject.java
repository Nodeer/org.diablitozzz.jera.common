package org.diablitozzz.jera.json;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonObject implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static JsonObject create() {
        return new JsonObject();
    }
    
    private boolean isArray = true;
    private final Map<String, Object> storage = new LinkedHashMap<String, Object>();
    private int listCounter = 0;
    
    public JsonObject add(final Object value) {
        this.storage.put(String.valueOf(this.listCounter), value);
        this.listCounter++;
        return this;
    }
    
    public JsonObject addTo(final JsonObject dist) {
        dist.add(this);
        return this;
    }
    
    public JsonObject clear() {
        this.storage.clear();
        return this;
    }
    
    public boolean containsKey(final Object key) {
        return this.storage.containsKey(key.toString());
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof JsonObject)) {
            return false;
        }
        
        //TODO переделать нормально
        return this.toJson().equals(((JsonObject) object).toJson());
    }
    
    public Object get(final Object key) {
        return this.storage.get(key.toString());
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(final Object key, final Class<T> cl) {
        return (T) this.storage.get(key.toString());
    }
    
    public JsonObject[] getArray(final Object key) {
        if (!this.containsKey(key)) {
            return new JsonObject[] {};
        }
        return this.getJsonObject(key).toArray();
    }
    
    public boolean getBoolean(final Object key) {
        return JsonUtilObject.toBoolean(this.get(key));
    }
    
    public Boolean getBooleanObject(final Object key) {
        return JsonUtilObject.toBooleanObject(this.get(key));
    }
    
    public Calendar getCalendarFromTimeStamp(final Object key) {
        
        final long timeStamp = JsonUtilObject.toLong(this.get(key));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        return calendar;
    }
    
    public double getDouble(final Object key) {
        return JsonUtilObject.toDouble(this.get(key));
    }
    
    public Double getDoubleObject(final Object key) {
        return JsonUtilObject.toDoubleObject(this.get(key));
    }
    
    public float getFloat(final Object key) {
        return JsonUtilObject.toFloat(this.get(key));
    }
    
    public Float getFloatObject(final Object key) {
        return JsonUtilObject.toFloatObject(this.get(key));
    }
    
    public int getInteger(final Object key) {
        return JsonUtilObject.toInteger(this.get(key));
    }
    
    public Integer getIntegerObject(final Object key) {
        return JsonUtilObject.toIntegerObject(this.get(key));
    }
    
    public JsonObject getJsonObject(final Object key) {
        return (JsonObject) this.get(key);
    }
    
    public Collection<String> getKeys() {
        return this.storage.keySet();
    }
    
    public Long[] getKeysAsLongArray() {
        final Set<String> keySet = this.storage.keySet();
        final Long[] out = new Long[keySet.size()];
        int i = 0;
        for (final String key : this.storage.keySet()) {
            out[i++] = JsonUtilObject.toLongObject(key);
        }
        return out;
    }
    
    public Collection<Long> getKeysAsLongObject() {
        final Collection<Long> out = new HashSet<Long>();
        for (final String key : this.storage.keySet()) {
            out.add(JsonUtilObject.toLongObject(key));
        }
        return out;
    }
    
    public long getLong(final Object key) {
        return JsonUtilObject.toLong(this.get(key));
    }
    
    /**
     * Возвращает пустой массив если данных нету
     */
    public Long[] getLongArray(final Object key) {
        
        if (!this.containsKey(key)) {
            return new Long[] {};
        }
        final JsonObject value = this.getJsonObject(key);
        if (value == null) {
            return new Long[] {};
        }
        final Object[] data = value.toObjectArray();
        final Long[] out = new Long[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = JsonUtilObject.toLongObject(data[i]);
        }
        return out;
    }
    
    public Long getLongObject(final Object key) {
        return JsonUtilObject.toLongObject(this.get(key));
    }
    
    public short getShort(final Object key) {
        return JsonUtilObject.toShort(this.get(key));
    }
    
    public Short getShortObject(final Object key) {
        return JsonUtilObject.toShortObject(this.get(key));
    }
    
    public String getString(final Object key) {
        return JsonUtilObject.toString(this.get(key));
    }
    
    /**
     * Возвращает пустой массив если данных нету
     */
    public String[] getStringArray(final Object key) {
        
        if (!this.containsKey(key)) {
            return new String[] {};
        }
        final JsonObject value = this.getJsonObject(key);
        if (value == null) {
            return new String[] {};
        }
        final Object[] data = value.toObjectArray();
        final String[] out = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = JsonUtilObject.toString(data[i]);
        }
        return out;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public boolean isArray() {
        return this.isArray;
    }
    
    public boolean optBoolean(final Object key, final boolean def) {
        if (this.storage.containsKey(key)) {
            return this.getBoolean(key);
        }
        return def;
    }
    
    public Boolean optBooleanObject(final Object key, final Boolean def) {
        if (this.storage.containsKey(key)) {
            return this.getBooleanObject(key);
        }
        return def;
    }
    
    public double optDouble(final Object key, final double def) {
        if (this.storage.containsKey(key)) {
            return this.getDouble(key);
        }
        return def;
    }
    
    public Double optDoubleObject(final Object key, final Double def) {
        if (this.storage.containsKey(key)) {
            return this.getDoubleObject(key);
        }
        return def;
    }
    
    public float optFloat(final Object key, final float def) {
        if (this.storage.containsKey(key)) {
            return this.getFloat(key);
        }
        return def;
    }
    
    public Float optFloat(final Object key, final Float def) {
        if (this.storage.containsKey(key)) {
            return this.getFloatObject(key);
        }
        return def;
    }
    
    public int optInteger(final Object key, final int def) {
        if (this.storage.containsKey(key)) {
            return this.getInteger(key);
        }
        return def;
    }
    
    public Integer optIntegerObject(final Object key, final Integer def) {
        if (this.storage.containsKey(key)) {
            return this.getIntegerObject(key);
        }
        return def;
    }
    
    public long optLong(final Object key, final long def) {
        if (this.storage.containsKey(key)) {
            return this.getLong(key);
        }
        return def;
    }
    
    public Long optLongObject(final Object key, final Long def) {
        if (this.storage.containsKey(key)) {
            return this.getLongObject(key);
        }
        return def;
    }
    
    public short optShort(final Object key, final short def) {
        if (this.storage.containsKey(key)) {
            return this.getShort(key);
        }
        return def;
    }
    
    public Short optShortObject(final Object key, final Short def) {
        if (this.storage.containsKey(key)) {
            return this.getShortObject(key);
        }
        return def;
    }
    
    public String optString(final Object key, final String def) {
        if (this.storage.containsKey(key)) {
            return this.getString(key);
        }
        return def;
    }
    
    public JsonObject remove(final Object key) {
        this.storage.remove(key);
        return this;
    }
    
    public JsonObject set(final Object key, final Object value) {
        this.isArray = false;
        this.storage.put(key.toString(), value);
        return this;
    }
    
    public JsonObject setAs(final Object key, final JsonObject dist) {
        dist.set(key, this);
        return this;
    }
    
    public JsonObject setTo(final JsonObject target, final Object key) {
        target.set(key, this);
        return this;
        
    }
    
    public int size() {
        return this.storage.size();
    }
    
    public JsonObject[] toArray() {
        
        final Object[] data = this.toObjectArray();
        final JsonObject[] out = new JsonObject[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = (JsonObject) data[i];
        }
        return out;
    }
    
    public String toJson() {
        return Json.encode(this);
    }
    
    public String toJson(final boolean longAsString) {
        return Json.encode(this, longAsString);
    }
    
    public Map<String, Object> toMap() {
        return this.storage;
    }
    
    public Object toMapOrArray() {
        if (this.isArray()) {
            return this.toObjectArray();
        }
        return this.toMap();
    }
    
    public Object[] toObjectArray() {
        return this.storage.values().toArray();
    }
    
    @Override
    public String toString() {
        return Json.encode(this);
    }
    
    public String[] toStringArray() {
        final Object[] items = this.toObjectArray();
        final String[] out = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            out[i] = items[i].toString();
        }
        return out;
    }
}
