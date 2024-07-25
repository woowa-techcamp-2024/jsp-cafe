package org.example.jspcafe.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateTimeString() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
