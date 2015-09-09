package org.diablitozzz.jera.spec;

abstract public class SpecItem<T> {
    
    private T value;
    private final boolean notNull;
    private final String errorMessage;

    public SpecItem(final T value, final boolean notNull, final String errorMessage) {
        this.value = value;
        this.notNull = notNull;
        this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public T getValue() {
        return this.value;
    }

    abstract public String getValueAsString();

    public boolean isNotNull() {
        return this.notNull;
    }
    
    public boolean setValue(final T value) {
        if (this.validate(value)) {
            this.value = value;
            return true;
        }
        return false;
    }

    abstract public boolean setValueAsObject(Object newValue);
    
    abstract public boolean validate(T value);
    
}
