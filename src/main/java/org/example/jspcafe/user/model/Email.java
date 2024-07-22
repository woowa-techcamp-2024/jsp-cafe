package org.example.jspcafe.user.model;

public class Email {

    private final String value;

    public String getValue() {
        return value;
    }

    public Email(String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Email은 null이거나 빈 문자열일 수 없습니다.");
        }
        if (!value.contains("@")) {
            throw new IllegalArgumentException("Email은 @를 포함해야 합니다.");
        }
    }
}
