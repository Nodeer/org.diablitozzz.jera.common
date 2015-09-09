package org.diablitozzz.jera.util;

public class UtilMath {
    
    public static double getAngle(final double x0, final double y0, final double x1, final double y1) {
        final double angle = Math.atan2(y1 - y0, x1 - x0) / Math.PI * 180D;
        return angle;
    }

    public static double quant(final double value, final double step, final double prec) {
        final double p = Math.pow(10, prec);
        final double valueFix = Math.round((value * p));
        final double stepFix = Math.round((step * p));
        final double delta = valueFix - valueFix % stepFix;
        return delta / p;
    }

    public static double round(final double value, final double prec) {
        final double p = Math.pow(10, prec);
        return Math.round(value * p) / p;
    }

    public static String toString(final double d) {
        if (d != Math.ceil(d)) {
            return String.valueOf(d);
        }
        return String.valueOf((long) d);
    }
}
