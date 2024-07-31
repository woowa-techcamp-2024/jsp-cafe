package com.woowa.cafe.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpMessageUtils {

    public static final String NEW_LINE = System.lineSeparator();

    public static Map<String, String> getBodyFormData(final HttpServletRequest req) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(URLDecoder.decode(line, StandardCharsets.UTF_8)).append(NEW_LINE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.stream(requestBody.toString().split("&"))
                .map(param -> param.split("=", 2))
                .filter(entry -> entry.length == 2)
                .collect(Collectors.toMap(entry -> entry[0], entry -> sanitize(entry[1])));
    }

    private static String sanitize(String input) {
        return input.replaceAll("<[^>]*>", "");
    }
}
