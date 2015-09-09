package org.diablitozzz.jera.concurrency.daemon;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class DaemonServerBasicThread implements DaemonServer, Runnable {

    private final Thread thread;
    private final DaemonServerBasic server;
    private boolean running = false;

    public DaemonServerBasicThread()
    {
        this.thread = new Thread(this);
        this.thread.setName(this.getClass().getCanonicalName());
        this.server = new DaemonServerBasic();
    }

    @Override
    public void addWorker(DaemonWorker worker) {
        this.server.addWorker(worker);
    }

    @Override
    public void clear() throws DaemonException {
        this.server.clear();
    }

    @Override
    public DaemonLogger getLogger() {
        return this.server.getLogger();
    }

    @Override
    public Collection<DaemonWorker> getWorkers() {
        return this.server.getWorkers();
    }

    @Override
    public boolean isStarted() {
        return this.server.isStarted();
    }

    @Override
    public void join() throws DaemonException {
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            throw new DaemonException(e);
        }
    }

    @Override
    public void run() {

        this.server.start();

        while (this.running) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
        this.server.stop();
    }

    @Override
    public void setLogger(DaemonLogger logger) {
        this.server.setLogger(logger);

    }

    @Override
    public void start() {
        this.running = true;
        this.thread.start();

    }

    @Override
    public void stop() {
        this.running = false;
    }

}
