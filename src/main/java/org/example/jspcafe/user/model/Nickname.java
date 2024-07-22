package org.example.jspcafe.user.model;

public class Nickname {

    private final String value;

    public String getValue() {
        return value;
    }

    public Nickname(String value) {
        validateNickname(value);
        validateLength(value);
        this.value = value;
    }

    private void validateNickname(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Nickname은 null이거나 빈 문자열일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() < 3 || value.length() > 15) {
            throw new IllegalArgumentException("Nickname 3자 이상 15자 이하여야 합니다.");
        }
    }
}
