package com.woowa.framework.web;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    boolean isSupports(HttpServletRequest request);
    HandlerMethod getHandlerMethod(HttpServletRequest request);
}
