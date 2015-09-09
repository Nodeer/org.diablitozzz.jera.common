package org.diablitozzz.jera.spec;

import org.diablitozzz.jera.json.JsonUtilObject;

public class SpecItemString extends SpecItem<String> {

    private final int minLength;
    private final int maxLength;
    
    public SpecItemString(final String value, final boolean notNull, final Integer minLength, final Integer maxLength, final String errorMessage) {
        super(value, notNull, errorMessage);
        this.minLength = minLength == null ? 0 : minLength;
        this.maxLength = maxLength == null ? Integer.MAX_VALUE : maxLength;
    }

    @Override
    public String getValueAsString() {
        return this.getValue();
    }
    
    @Override
    public boolean setValueAsObject(final Object newValue) {
        try {
            return this.setValue(JsonUtilObject.toString(newValue));
        } catch (final Throwable e) {
            return false;
        }
    }
    
    @Override
    public boolean validate(final String value) {
        if (value == null && this.isNotNull()) {
            return false;
        }
        try {
            final String sValue = value.toString();
            if ((sValue.length() < this.minLength) || (sValue.length() > this.maxLength)) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }
    
}
