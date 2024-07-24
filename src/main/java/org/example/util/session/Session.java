package org.example.util.session;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;
import java.time.Instant;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session implements HttpSession {

    public static final String SESSION_ID = "SESSIONID";

    private final String id;
    private final Map<String, Object> attributes = new HashMap<>();
    private Instant lastAccess;
    private final Instant creationTime;
    private int maxInactiveInterval = 1800; // 30 minutes default
    private boolean isNew = true;
    private boolean isValid = true;

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.lastAccess = Instant.now();
        this.creationTime = Instant.now();
    }

    @Override
    public long getCreationTime() {
        return creationTime.toEpochMilli();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccess.toEpochMilli();
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("ServletContext not supported in this implementation");
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("HttpSessionContext is deprecated");
    }

    @Override
    public Object getAttribute(String name) {
        access();
        return attributes.get(name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        access();
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public String[] getValueNames() {
        access();
        return attributes.keySet().toArray(new String[0]);
    }

    @Override
    public void setAttribute(String name, Object value) {
        access();
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        access();
        attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
        isValid = false;
        attributes.clear();
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void access() {
        this.lastAccess = Instant.now();
        this.isNew = false;
    }

    public boolean isValid() {
        return isValid;
    }

    // 기존 메소드들 유지
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Instant getLastAccess() {
        return lastAccess;
    }


    public void setLastAccess(Instant lastAccess) {
        this.lastAccess = lastAccess;
    }

    public void setAttributes(String name, Object value) {
        setAttribute(name, value);
    }
}