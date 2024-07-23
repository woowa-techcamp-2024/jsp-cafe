package org.example.jspcafe.post.model;

public class Title {

    private final String value;

    public String getValue() {
        return value;
    }

    public Title(String title) {
        validateTitle(title);
        validateLength(title);
        this.value = title;
    }

    private void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목이 없습니다.");
        }
    }

    private void validateLength(String title) {
        if (title.length() > 30) {
            throw new IllegalArgumentException("제목은 30자 이하여야 합니다.");
        }
    }

}
