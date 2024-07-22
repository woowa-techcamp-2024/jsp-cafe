package com.woowa.framework.web;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntity {

    private final Map<String, Object> model;
    private final int status;
    private String location = "";

    public ResponseEntity(Map<String, Object> model, int status) {
        this(model, status, "");
    }

    public ResponseEntity(Map<String, Object> model, int status, String location) {
        this.model = model;
        this.status = status;
        this.location = location;
    }

    public void add(String key, Object value) {
        model.put(key, value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, Object> model = new HashMap<>();
        private int status = 200;

        public Builder add(String key, Object value) {
            model.put(key, value);
            return this;
        }

        public ResponseEntity created() {
            return new ResponseEntity(model, 201);
        }

        public ResponseEntity found(String location) {
            return new ResponseEntity(model, 302, location);
        }
    }
}
