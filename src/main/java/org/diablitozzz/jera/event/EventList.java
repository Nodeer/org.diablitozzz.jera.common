package org.diablitozzz.jera.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.diablitozzz.jera.func.Func;

public class EventList {

    private final Queue<Func> handlers = new ConcurrentLinkedQueue<>();
    private volatile boolean enable = true;
    
    public void add(final Func e) {
        this.handlers.add(e);
    }
    
    public void fire() {
        if (!this.enable) {
            return;
        }
        for (final Func handler : this.handlers) {
            if (this.enable) {
                handler.invoke();
            }
        }
    }
    
    public boolean isEnable() {
        return this.enable;
    }

    public void remove(final Func e) {
        this.handlers.remove(e);
    }
    
    public void setEnable(final boolean enable) {
        this.enable = enable;
    }

}
