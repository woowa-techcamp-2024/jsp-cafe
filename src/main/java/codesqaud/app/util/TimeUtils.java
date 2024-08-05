package codesqaud.app.util;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter FORMAT_FOR_USER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FORMAT_FOR_QUERY = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        return OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Asia/Seoul"));
    }

    public static String toStringForUser(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(FORMAT_FOR_USER);
    }

    public static String toStringForUser(Timestamp timestamp) {
        OffsetDateTime offsetDateTime = toOffsetDateTime(timestamp);
        return toStringForUser(offsetDateTime);
    }

    public static String toStringForQuery(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toLocalDateTime().format(FORMAT_FOR_QUERY);
    }
}
