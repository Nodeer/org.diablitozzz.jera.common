package org.diablitozzz.jera.func;

public interface FuncApplyWithException<P, E extends Throwable> {
    
    void invoke(P param) throws E;
    
}
