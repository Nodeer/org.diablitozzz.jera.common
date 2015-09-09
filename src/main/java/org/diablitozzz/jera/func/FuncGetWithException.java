package org.diablitozzz.jera.func;

public interface FuncGetWithException<R, E extends Throwable> {
    
    R invoke() throws E;
    
}
