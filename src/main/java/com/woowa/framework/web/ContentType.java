package com.woowa.framework.web;

import java.util.Arrays;
import java.util.Set;

public enum ContentType {
    TEXT_HTML(Set.of("html", "jsp"), "text/html"),
    TEXT_CSS(Set.of("css"), "text/css"),
    TEXT_JAVASCRIPT(Set.of("js"), "text/javascript"),
    IMAGE_JPEG(Set.of("jpg", "jpeg"), "image/jpeg"),
    IMAGE_PNG(Set.of("png"), "image/png"),
    IMAGE_X_ICO(Set.of("ico"), "image/x-icon"),
    IMAGE_SVG(Set.of("svg"), "image/svg+xml"),
    FONT_TTF(Set.of("ttf"), "font/ttf"),
    FONT_WOFF(Set.of("woff"), "font/woff"),
    FONT_WOFF2(Set.of("woff2"), "font/woff2"),;

    private final Set<String> fileExtension;
    private final String contentType;

    ContentType(Set<String> fileExtension, String contentType) {
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }

    public static String getContentType(String fileExtension) {
        return Arrays.stream(values())
                .filter(value -> value.fileExtension.contains(fileExtension))
                .findFirst()
                .map(value -> value.contentType)
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 content-type 입니다. fileExtension: " + fileExtension));
    }

    public Set<String> getFileExtension() {
        return fileExtension;
    }

    public String getContentType() {
        return contentType;
    }
}
