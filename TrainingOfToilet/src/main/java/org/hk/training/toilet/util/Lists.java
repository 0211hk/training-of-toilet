package org.hk.training.toilet.util;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Lists {

    private Lists() {
        throw new UnsupportedOperationException();
    }

    public static <T> ArrayList<T> newEmptyArrayList() {
        return new ArrayList<T>();
    }

    public static <T> ArrayList<T> newEmptyArrayList(T... ts) {
        ArrayList<T> l = new ArrayList<T>(ts.length);
        for (final T t : ts) {
            l.add(t);
        }
        return l;
    }

    public static <T> CopyOnWriteArrayList<T> newEmptyCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<T>();
    }
}
