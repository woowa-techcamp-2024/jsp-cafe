package org.example.config.controller;

public class ParameterInfo {
    private final Class<?> type;
    private final boolean required;
    private final String defaultValue;

    public ParameterInfo(Class<?> type, boolean required, String defaultValue) {
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}