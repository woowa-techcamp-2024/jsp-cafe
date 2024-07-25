package com.hyeonuk.jspcafe.member.servlets.mock;

import java.util.HashMap;
import java.util.Map;

public class MockServletContext extends BaseServletContext {
    private Map<String,Object> attributes = new HashMap<>();

    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }
    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }
}