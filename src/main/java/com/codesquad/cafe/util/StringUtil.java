package com.codesquad.cafe.util;

public final class StringUtil {
	private StringUtil() {
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}
}
