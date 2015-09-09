package org.diablitozzz.jera.validator;

abstract public class ValidatorObject {

    private final boolean notNull;
    private final String errorMessage;
    
    public ValidatorObject(final boolean notNull, final String errorMessage) {
        this.notNull = notNull;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public boolean isNotNull() {
        return this.notNull;
    }

    public boolean isValid(final Object value) {
        if (value == null && this.isNotNull()) {
            return false;
        }
        return true;
    }

}
