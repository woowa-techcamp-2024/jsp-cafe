package com.woowa.framework.web.mapping;

import com.woowa.framework.web.ResponseEntity;
import java.lang.reflect.Parameter;

public interface HandlerMethod {
    ResponseEntity invoke(Object[] args) throws Throwable;

    Parameter[] getParameters();
}
