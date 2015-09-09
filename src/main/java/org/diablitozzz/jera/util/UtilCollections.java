package org.diablitozzz.jera.util;

import java.util.ArrayList;
import java.util.List;

public class UtilCollections {

    private static boolean equals(final Object objectA, final Object objectB) {
        if (objectA == objectB) {
            return true;
        }
        if (objectA == null) {
            return false;
        }
        return objectA.equals(objectB);
    }

    public static <T> List<T> join(final List<T> listA, final List<T> listB) {
        final List<T> out = new ArrayList<>(listA.size() + listB.size());
        out.addAll(listA);
        out.addAll(listB);
        return out;
    }

    public static <T> void moveAfter(final T[] data, final T value, final T after) {

        final Object[] tmp = new Object[data.length];
        int i = 0;
        for (final T item : data) {
            if (UtilCollections.equals(item, value)) {
                continue;
            }
            tmp[i++] = item;
            if (UtilCollections.equals(item, after)) {
                tmp[i++] = value;
            }
        }
        System.arraycopy(tmp, 0, data, 0, tmp.length);
    }

    public static <T> void moveBefore(final T[] data, final T value, final T before) {

        final Object[] tmp = new Object[data.length];
        int i = 0;
        for (final T item : data) {
            if (UtilCollections.equals(item, value)) {
                continue;
            }
            if (UtilCollections.equals(item, before)) {
                tmp[i++] = value;
            }
            tmp[i++] = item;
        }
        System.arraycopy(tmp, 0, data, 0, tmp.length);
    }

    public static <T> void moveFirst(final T[] data, final T value) {

        final Object[] tmp = new Object[data.length];
        tmp[0] = value;
        int i = 1;
        for (final T item : data) {
            if (UtilCollections.equals(item, value)) {
                continue;
            }
            tmp[i++] = item;
        }
        System.arraycopy(tmp, 0, data, 0, tmp.length);
    }
}
