package codesqaud.mock;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MockHttpSession implements HttpSession {
    private Map<String, Object> attributes = new HashMap<>();
    private boolean isInvalidated;

    public boolean isInvalidated() {
        return isInvalidated;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void invalidate() {
        isInvalidated = true;
        attributes.clear();
    }

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
    public void setMaxInactiveInterval(int interval) {
    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }


    @Override
    public void removeAttribute(String name) {
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
