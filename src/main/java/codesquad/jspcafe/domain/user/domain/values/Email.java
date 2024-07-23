package codesquad.jspcafe.domain.user.domain.values;

import java.util.regex.Pattern;

public class Email {

    private static final Pattern PATTERN = Pattern.compile(
        "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    private final String value;

    private Email(String value) {
        if (value == null || value.isBlank() || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        this.value = value;
    }

    public static Email from(String value) {
        return new Email(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email email)) {
            return false;
        }

        return getValue().equals(email.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
