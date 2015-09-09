package org.diablitozzz.jera.spec;

import org.diablitozzz.jera.json.JsonUtilObject;

public class SpecItemDouble extends SpecItem<Double> {

    private final double minValue;
    private final double maxValue;
    
    public SpecItemDouble(final Double value, final boolean notNull, final Double minValue, final Double maxValue, final String errorMessage) {
        super(value, notNull, errorMessage);
        this.minValue = minValue == null ? Double.MIN_VALUE : minValue;
        this.maxValue = maxValue == null ? Double.MAX_VALUE : maxValue;
    }
    
    @Override
    public String getValueAsString() {
        return this.getValue().toString();
    }

    @Override
    public boolean setValueAsObject(final Object newValue) {
        try {
            return this.setValue(JsonUtilObject.toDoubleObject(newValue));
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public boolean validate(final Double value) {
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
