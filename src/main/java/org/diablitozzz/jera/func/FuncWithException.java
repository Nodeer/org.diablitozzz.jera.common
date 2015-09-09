package org.diablitozzz.jera.func;

public interface FuncWithException<E extends Throwable> {

    void invoke() throws E;

}
