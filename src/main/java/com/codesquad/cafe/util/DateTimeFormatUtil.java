package com.codesquad.cafe.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatUtil {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private DateTimeFormatUtil() {
	}

	public static String getFormattedDate(LocalDateTime dateTime) {
		return dateTime.format(formatter);
	}

}
