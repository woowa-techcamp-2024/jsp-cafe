package com.woowa.hyeonsik.application.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTimeUtil() {}

    /**
     * 문자열을 입력받아 LocalDateTime으로 반환합니다.
     * @param localDateTime yyyy-MM-dd hh:mm:ss 형식의 문자열 값
     * @return
     */
    public static LocalDateTime from(String localDateTime) {
        final String[] split = localDateTime.split("\\.");
        return LocalDateTime.parse(split[0], formatter);
    }
}
