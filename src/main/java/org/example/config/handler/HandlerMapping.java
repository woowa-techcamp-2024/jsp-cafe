package org.example.config.handler;

import java.util.Map.Entry;
import org.example.config.HttpMethod;
import org.example.config.controller.MethodHandler;
import org.example.config.handler.AnnotationHandlerMapping.HandlerKey;

public interface HandlerMapping {

    Entry<HandlerKey, MethodHandler> getHandler(String path, HttpMethod method);
}
