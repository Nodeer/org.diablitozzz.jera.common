package org.diablitozzz.jera.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.diablitozzz.jera.util.UtilObject;

public class Props {

    private final Properties props = new Properties();
    
    public boolean containsKey(final String key) {
        return this.props.containsKey(key);
    }
    
    public Boolean getBoolean(final String key) {
        return UtilObject.toBooleanObject(this.props.getProperty(key));
    }
    
    public Double getDouble(final String key) {
        return UtilObject.toDoubleObject(this.props.getProperty(key));
    }

    public Integer getInteger(final String key) {
        return UtilObject.toIntegerObject(this.props.getProperty(key));
    }

    public String getString(final String key) {
        return this.props.getProperty(key);
    }
    
    public void load(final File file) throws IOException {
        try (FileInputStream inpuStream = new FileInputStream(file)) {
            this.props.loadFromXML(inpuStream);
        }
    }

    public void save(final File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            this.props.storeToXML(outputStream, "");
        }
    }
    
    public void set(final String key, final Object value) {
        if (value == null) {
            this.props.remove(key);
        } else {
            this.props.setProperty(key, value.toString());
        }
    }

}
