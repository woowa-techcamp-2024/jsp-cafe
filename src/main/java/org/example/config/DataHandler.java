package org.example.config;

public enum DataHandler {
    USER("userDataHandler"),
    ARTICLE("articleDataHandler")
    ;
    private final String value;

    DataHandler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
