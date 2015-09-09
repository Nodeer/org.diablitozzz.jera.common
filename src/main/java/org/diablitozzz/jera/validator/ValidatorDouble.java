package org.diablitozzz.jera.validator;

import org.diablitozzz.jera.util.UtilObject;

public class ValidatorDouble extends ValidatorObject {

    private final double minValue;
    private final double maxValue;
    
    public ValidatorDouble(final boolean notNull, final Double minValue, final Double maxValue, final String errorMessage) {
        super(notNull, errorMessage);
        this.minValue = minValue == null ? Double.MIN_VALUE : minValue;
        this.maxValue = maxValue == null ? Double.MAX_VALUE : maxValue;
    }

    @Override
    public boolean isValid(final Object value) {
        if (!super.isValid(value)) {
            return false;
        }
        if (value == null) {
            return true;
        }
        try {
            final double val = UtilObject.toDouble(value);
            if ((val < this.minValue) || (val > this.maxValue)) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }
    
}
