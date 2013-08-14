package org.hk.training.toilet.util;

public final class Booleans {

    private Booleans() {
        throw new UnsupportedOperationException();
    }

    public final static String toString(final boolean b) {
        return String.valueOf(b);
    }

    public final static int toInt(final boolean b) {
        return b ? 1 : 0;
    }

    public final static String toStringAsInt(final boolean b) {
        int i = b ? 1 : 0;
        return String.valueOf(i);
    }
}
