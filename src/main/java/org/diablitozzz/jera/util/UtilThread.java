package org.diablitozzz.jera.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class UtilThread {
    
    public interface CallBackWithException {
        void call() throws Throwable;
    }

    public interface WaitCallBack {
        boolean run() throws Exception;
    }

    public static Thread createTimer(final String name, final boolean daemon, final long delayMs, final long errorDelayMs, final CallBackWithException callBack, final Consumer<Throwable> onError) {

        final Thread thread = new Thread(() -> {
            while (true) {
                try {
                    callBack.call();
                    TimeUnit.MILLISECONDS.sleep(delayMs);
                } catch (final Throwable e) {
                    if (e instanceof InterruptedException) {
                        return;
                    }
                    try {
                        onError.accept(e);
                        TimeUnit.MILLISECONDS.sleep(errorDelayMs);
                    } catch (final Throwable e1) {
                        return;
                    }
                }
            }
        } , name);
        thread.setDaemon(daemon);
        return thread;
    }

    public static boolean wait(final int count, final long sleepMs, final WaitCallBack callBack) throws Exception {
        int i;
        for (i = 0; i <= count; i++) {
            final boolean result;
            try {
                result = callBack.run();
            } catch (final Throwable e) {
                return false;
            }
            if (!result) {
                TimeUnit.MILLISECONDS.sleep(sleepMs);
            } else {
                return true;
            }
        }
        return false;
    }
    
}
