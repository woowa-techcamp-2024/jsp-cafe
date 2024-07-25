package org.example.cafe.utils;

import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateTimeFormatUtils() {
    }

    public static String formatDateTime(java.time.LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
