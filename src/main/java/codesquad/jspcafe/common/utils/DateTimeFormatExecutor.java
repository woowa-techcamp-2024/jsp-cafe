package codesquad.jspcafe.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatExecutor {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    private DateTimeFormatExecutor() {
    }

    public static String execute(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMAT);
    }
}
