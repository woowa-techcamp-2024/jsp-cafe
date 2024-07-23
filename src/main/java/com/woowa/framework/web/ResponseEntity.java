package com.woowa.framework.web;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntity {

    private final Map<String, Object> model = new HashMap<>();
    private final Map<String, String> header = new HashMap<>();
    private final int status;
    private String location = "";
    private final byte[] body;

    public ResponseEntity(int status, byte[] body) {
        this(status, "", body);
    }

    public ResponseEntity(int status, String location, byte[] body) {
        this.status = status;
        this.location = location;
        this.body = body;
    }

    public void add(String key, Object value) {
        model.put(key, value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, Object> model = new HashMap<>();
        private Map<String, String> header = new HashMap<>();

        public Builder add(String key, Object value) {
            model.put(key, value);
            return this;
        }

        public Builder addHeader(String key, String value) {
            header.put(key, value);
            return this;
        }

        public ResponseEntity found(String location) {
            ResponseEntity responseEntity = new ResponseEntity(302, location, new byte[0]);
            responseEntity.model.putAll(model);
            responseEntity.header.putAll(header);
            return responseEntity;
        }
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public int getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public byte[] getBody() {
        return body;
    }
}
