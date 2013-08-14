package org.hk.training.toilet.util;

public final class IntegerUtil {

    private IntegerUtil() {
        throw new UnsupportedOperationException();
    }

    public static final int toInt(final String i, final int defaultVal) {
        try {
            return Integer.parseInt(i);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static final double toDouble(final String i, final int defaultVal) {
        try {
            return Double.parseDouble(i);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static final boolean isInt(final String i) {
        try {
            Integer.parseInt(i);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final boolean isLong(final String i) {
        try {
            Long.parseLong(i);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final boolean isFloat(final String i) {
        try {
            Float.parseFloat(i);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final boolean isDouble(final String i) {
        try {
            Double.parseDouble(i);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
