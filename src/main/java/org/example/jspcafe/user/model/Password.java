package org.example.jspcafe.user.model;

public class Password {

    private final String value;

    public String getValue() {
        return value;
    }

    public Password(String value) {
        validatePassword(value);
        valdateLength(value);
        this.value = value;
    }

    private void validatePassword(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Password는 null이거나 빈 문자열일 수 없습니다.");
        }
    }

    private void valdateLength(String value) {
        if (value.length() < 8 || value.length() > 20) {
            throw new IllegalArgumentException("Password는 8자 이상 20자 이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;

        return getValue() != null ? getValue().equals(password.getValue()) : password.getValue() == null;
    }

    @Override
    public int hashCode() {
        return getValue() != null ? getValue().hashCode() : 0;
    }
}
