package com.woowa.framework.web;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT;

    public static HttpMethod from(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메서드입니다."));
    }
}
