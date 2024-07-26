package org.example.config.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.example.config.HttpMethod;
import org.example.config.annotation.Component;
import org.example.config.controller.MethodHandler;
import org.example.util.UrlMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private Map<HandlerKey, MethodHandler> handlerMap = new HashMap<>();

    @Override
    public Entry<HandlerKey, MethodHandler> getHandler(String path, HttpMethod method) {
        logger.info("핸들러 경로 : {} {}", method, path);
        for (Entry<HandlerKey, MethodHandler> entry : handlerMap.entrySet()) {
            HandlerKey key = entry.getKey();
            logger.info(key.toString());
            if (key.getHttpMethod() == method && UrlMatcher.match(key.getUrl(), path)) {
                return entry;
            }
        }
        return null;
    }


    public static class HandlerKey {
        private String url;
        private HttpMethod httpMethod;

        public HandlerKey(String url, HttpMethod httpMethod) {
            this.url = url;
            this.httpMethod = httpMethod;
        }

        @Override
        public String toString() {
            return "HandlerKey{" +
                    "url='" + url + '\'' +
                    ", httpMethod=" + httpMethod +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HandlerKey that = (HandlerKey) o;
            return Objects.equals(url, that.url) && httpMethod == that.httpMethod;
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, httpMethod);
        }

        public String getUrl() {
            return url;
        }

        public HttpMethod getHttpMethod() {
            return httpMethod;
        }
    }
}
