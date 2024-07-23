package com.woowa.framework.web.mapping;

import com.woowa.framework.web.ResponseEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;

public class StaticResourceHandlerMethod implements HandlerMethod {

    private final String resourcePath;
    private final String resourceName;

    public StaticResourceHandlerMethod(String resourcePath, String resourceName) {
        this.resourcePath = resourcePath;
        this.resourceName = resourceName;
    }

    public ResponseEntity invoke(Object[] args) {
        File file = new File(resourcePath, resourceName);
        try (FileInputStream fis = new FileInputStream(file)) {
            return ResponseEntity.builder().body(fis.readAllBytes()).ok();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parameter[] getParameters() {
        return new Parameter[0];
    }
}
