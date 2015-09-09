package org.diablitozzz.jera.spec;

import org.diablitozzz.jera.json.JsonUtilObject;

public class SpecItemBoolean extends SpecItem<Boolean> {

    public SpecItemBoolean(final Boolean value, final boolean notNull) {
        this(value, notNull, "");
    }
    
    public SpecItemBoolean(final Boolean value, final boolean notNull, final String errorMessage) {
        super(value, notNull, errorMessage);
    }

    @Override
    public String getValueAsString() {
        return this.getValue() ? "1" : "0";
    }

    @Override
    public boolean setValueAsObject(final Object newValue) {
        try {
            return this.setValue(JsonUtilObject.toBooleanObject(newValue));
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public boolean validate(final Boolean value) {
        if (value == null && this.isNotNull()) {
            return false;
        }
        return true;
    }
    
}
