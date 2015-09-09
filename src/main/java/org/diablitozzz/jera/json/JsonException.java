
package org.diablitozzz.jera.json;

public class JsonException extends Exception {
    
    private static final long serialVersionUID = 1209616288149695031L;
    
    public JsonException(final String msg) {
        super(msg);
    }
    
    public JsonException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }
    
    public JsonException(final Throwable throwable) {
        super(throwable);
    }
}
