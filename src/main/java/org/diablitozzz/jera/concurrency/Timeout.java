package org.diablitozzz.jera.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Timeout {
    
    public static boolean timeout(final long timeoutMillis, final long delay, final Supplier<Boolean> callBack) {

        final long start = System.currentTimeMillis();
        while (true) {
            if (callBack.get()) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (final InterruptedException e) {
                return false;
            }
            if ((System.currentTimeMillis() - start) >= timeoutMillis) {
                return false;
            }
        }
    }
}
