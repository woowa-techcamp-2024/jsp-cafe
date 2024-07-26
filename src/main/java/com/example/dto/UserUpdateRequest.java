package com.example.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.annotation.NotNull;
import com.example.dto.util.DtoValidationUtil;

public record UserUpdateRequest(
	String name,
	String email,
	@NotNull String password
) {

	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z A-Z]{2,7}$";
	private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

	public void validate() {
		DtoValidationUtil.validate(this);
		if (email != null) {
			Matcher emailMatcher = emailPattern.matcher(email);
			if (!emailMatcher.matches()) {
				throw new RuntimeException("Invalid email pattern");
			}
		}
		if (name != null && name.length() > 100) {
			throw new RuntimeException("name is too long");
		}
	}
}
