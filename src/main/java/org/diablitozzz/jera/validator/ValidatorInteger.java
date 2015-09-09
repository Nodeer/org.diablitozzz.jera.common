package org.diablitozzz.jera.validator;

import org.diablitozzz.jera.util.UtilObject;

public class ValidatorInteger extends ValidatorObject {

    private final int minValue;
    private final int maxValue;
    
    public ValidatorInteger(final boolean notNull, final Integer minValue, final Integer maxValue, final String errorMessage) {
        super(notNull, errorMessage);
        this.minValue = minValue == null ? Integer.MIN_VALUE : minValue;
        this.maxValue = maxValue == null ? Integer.MAX_VALUE : maxValue;
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
            final int val = UtilObject.toInteger(value);
            if ((val < this.minValue) || (val > this.maxValue)) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }

}
