package org.diablitozzz.jera.validator;

import org.diablitozzz.jera.util.UtilObject;

public class ValidatorString extends ValidatorObject {

    private final int minLength;
    private final int maxLength;
    
    public ValidatorString(final boolean notNull, final Integer minLength, final Integer maxLength, final String errorMessage) {
        super(notNull, errorMessage);
        this.minLength = minLength == null ? 0 : minLength;
        this.maxLength = maxLength == null ? Integer.MAX_VALUE : maxLength;
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
            final String val = UtilObject.toString(value);
            if ((val.length() < this.minLength) || (val.length() > this.maxLength)) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
        return true;
    }
    
}
