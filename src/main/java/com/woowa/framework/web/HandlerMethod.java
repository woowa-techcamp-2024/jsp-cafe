package com.woowa.framework.web;

import java.lang.reflect.Parameter;

public interface HandlerMethod {
    ResponseEntity invoke(Object[] args) throws Throwable;

    Parameter[] getParameters();
}
