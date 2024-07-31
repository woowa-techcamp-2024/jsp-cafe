package com.codesquad.cafe.util;

public final class StringUtils {
    private StringUtils(){}
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
