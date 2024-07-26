package com.example.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.annotation.NotNull;
import com.example.dto.util.DtoValidationUtil;

public record SignupRequest(
	@NotNull String id,
	@NotNull String password,
	@NotNull String name,
	@NotNull String email
) {

	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z A-Z]{2,7}$";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$";
	private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

	public void validate() {
		DtoValidationUtil.validate(this);
		Matcher emailMatcher = emailPattern.matcher(email);
		Matcher passwordMatcher = passwordPattern.matcher(password);

		if (!emailMatcher.matches()) {
			throw new RuntimeException("Invalid email pattern");
		}
		if (!passwordMatcher.matches()) {
			throw new RuntimeException("Invalid password pattern");
		}
		if (id.length() > 100) {
			throw new RuntimeException("id is too long");
		}
		if (name.length() > 100) {
			throw new RuntimeException("name is too long");
		}
	}
}
