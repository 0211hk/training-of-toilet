package org.hk.training.toilet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ClassUtil {

    private ClassUtil() {
        throw new UnsupportedOperationException();
    }

    public static final String camelToSnake(String targetStr) {
        String convertedStr = targetStr.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll(
                "([a-z])([A-Z])", "$1_$2");
        return convertedStr.toLowerCase();
    }

    public static final String snakeToCamel(String targetStr) {
        Pattern p = Pattern.compile("_([a-z])");
        Matcher m = p.matcher(targetStr.toLowerCase());

        StringBuffer sb = new StringBuffer(targetStr.length());
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static final boolean isOrNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isAllNull(Object... objects) {
        int i = 0;
        for (Object o : objects) {
            if (o == null) {
                i = i + 1;
            }
        }
        if (i == objects.length) {
            return true;
        }
        return false;
    }
}
