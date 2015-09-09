package org.diablitozzz.jera.date;

import java.time.LocalTime;

public class TimeInterval {

    private final LocalTime from;
    private final LocalTime to;

    public TimeInterval(final LocalTime from, final LocalTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TimeInterval other = (TimeInterval) obj;
        if (this.from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!this.from.equals(other.from)) {
            return false;
        }
        if (this.to == null) {
            if (other.to != null) {
                return false;
            }
        } else if (!this.to.equals(other.to)) {
            return false;
        }
        return true;
    }

    public LocalTime getFrom() {
        return this.from;
    }
    
    public LocalTime getTo() {
        return this.to;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.from == null) ? 0 : this.from.hashCode());
        result = prime * result + ((this.to == null) ? 0 : this.to.hashCode());
        return result;
    }

    public boolean inInterval(final LocalTime time) {
        return this.from.compareTo(time) <= 0 && this.to.compareTo(time) >= 0;
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();
        out.append(this.from.toString());
        out.append(" - ");
        out.append(this.to.toString());
        return out.toString();
    }
}
