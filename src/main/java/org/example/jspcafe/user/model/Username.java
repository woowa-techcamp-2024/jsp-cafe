package org.example.jspcafe.user.model;

public class Username {

    private final String value;

    public String getValue() {
        return value;
    }

    public Username(String value) {
        validateUsername(value);
        validateLength(value);
        this.value = value;
    }

    private void validateUsername(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Username은 null이거나 빈 문자열일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() < 3 || value.length() > 15) {
            throw new IllegalArgumentException("Username은 3자 이상 15자 이하여야 합니다.");
        }
    }
}
