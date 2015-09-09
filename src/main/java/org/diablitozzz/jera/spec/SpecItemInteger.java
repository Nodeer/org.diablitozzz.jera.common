package org.diablitozzz.jera.spec;

import org.diablitozzz.jera.json.JsonUtilObject;

public class SpecItemInteger extends SpecItem<Integer> {
    
    private final int minValue;
    private final int maxValue;

    public SpecItemInteger(final Integer value, final boolean notNull, final Integer minValue, final Integer maxValue, final String errorMessage) {
        super(value, notNull, errorMessage);
        this.minValue = minValue == null ? Integer.MIN_VALUE : minValue;
        this.maxValue = maxValue == null ? Integer.MAX_VALUE : maxValue;
    }

    @Override
    public String getValueAsString() {
        return this.getValue().toString();
    }

    @Override
    public boolean setValueAsObject(final Object newValue) {
        try {
            return this.setValue(JsonUtilObject.toIntegerObject(newValue));
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public boolean validate(final Integer value) {
        if (value == null && this.isNotNull()) {
            return false;
        }
        try {
            if ((value < this.minValue) || (value > this.maxValue)) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }

}
