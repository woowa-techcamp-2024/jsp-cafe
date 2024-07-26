package com.hyeonuk.jspcafe.member.servlets.mock;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MockSession implements HttpSession {
    private final Map<String,Object> attributes = new HashMap<>();
    private boolean invalid = false;
    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s,o);
    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
