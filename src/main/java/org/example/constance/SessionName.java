package org.example.constance;

public enum SessionName {
    USER("user");
    private final String name;

    SessionName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
