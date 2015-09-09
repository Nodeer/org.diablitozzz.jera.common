package org.diablitozzz.jera.date;

import java.time.LocalDateTime;

public class DateTimeInterval {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public DateTimeInterval(final LocalDateTime from, final LocalDateTime to) {
        if (from.compareTo(to) <= 0) {
            this.from = from;
            this.to = to;
        } else {
            this.from = to;
            this.to = from;
        }
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
        final DateTimeInterval other = (DateTimeInterval) obj;
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

    public LocalDateTime getFrom() {
        return this.from;
    }
    
    public LocalDateTime getTo() {
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

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();
        out.append(this.from.toString());
        out.append(" - ");
        out.append(this.to.toString());
        return out.toString();
    }
    
}
