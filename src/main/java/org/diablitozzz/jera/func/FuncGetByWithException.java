package org.diablitozzz.jera.func;

public interface FuncGetByWithException<R, P, E extends Throwable> {
    
    R invoke(P param) throws E;
    
}
