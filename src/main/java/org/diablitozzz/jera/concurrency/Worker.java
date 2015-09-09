package org.diablitozzz.jera.concurrency;

import java.util.concurrent.TimeUnit;

import org.diablitozzz.jera.func.FuncWithException;
import org.diablitozzz.jera.log.LogLogger;

public class Worker {

    public static Worker create() {
        return new Worker();
    }

    private volatile LogLogger logger;
    private volatile String name;
    private volatile boolean starting = false;
    private boolean daemon = false;
    private volatile FuncWithException<Throwable> onIterate;
    private volatile Thread thread;
    
    private Worker() {
    }

    public LogLogger getLogger() {
        return this.logger;
    }

    public String getName() {
        return this.name;
    }

    public FuncWithException<Throwable> getOnIterate() {
        return this.onIterate;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public boolean isStarting() {
        return this.starting;
    }

    public Worker join() {
        try {
            this.thread.join();
        } catch (final Throwable e) {
        }
        return this;
    }

    public Worker setDaemon(final boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public Worker setLogger(final LogLogger logger) {
        this.logger = logger;
        return this;
    }
    
    public Worker setName(final String name) {
        this.name = name;
        return this;
    }

    public Worker setOnIterate(final FuncWithException<Throwable> onIterate) {
        this.onIterate = onIterate;
        return this;
    }

    public Worker start() {
        this.starting = true;
        this.thread = new Thread(() -> {
            while (this.starting) {
                try {
                    this.onIterate.invoke();
                } catch (final Throwable e) {
                    if (e instanceof InterruptedException) {
                        this.starting = false;
                    } else if (this.logger != null) {
                        this.logger.error(e);
                    }
                }
            }
        });
        if (this.name != null) {
            this.thread.setName(this.name);
        }
        this.thread.setDaemon(this.daemon);
        this.starting = true;
        this.thread.start();
        return this;
    }
    
    public Worker stop() {
        this.starting = false;
        if (this.thread == null) {
            return this;
        }
        //wait
        for (int i = 0; i < 1000; i++) {
            if (!this.thread.isAlive()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (final InterruptedException e) {
                break;
            }
        }
        while (this.thread.isAlive()) {
            this.thread.interrupt();
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (final InterruptedException e) {
                break;
            }
        }
        this.thread = null;
        return this;
    }

}
