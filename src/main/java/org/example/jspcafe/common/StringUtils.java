package org.example.jspcafe.common;

public class StringUtils {
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // 정수 또는 실수를 포함한 숫자 패턴
    }
}
