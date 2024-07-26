package org.example.util;

import java.util.HashMap;
import java.util.Map;

public class UrlMatcher {
    public static boolean match(String pattern, String path) {
        String[] patternParts = pattern.split("/");
        String[] pathParts = path.split("/");

        if (patternParts.length != pathParts.length) {
            return false;
        }

        for (int i = 0; i < patternParts.length; i++) {
            if (!patternParts[i].startsWith("{") && !patternParts[i].equals(pathParts[i])) {
                return false;
            }
        }

        return true;
    }

    public static Map<String, String> extractPathVariables(String pattern, String path) {
        Map<String, String> variables = new HashMap<>();
        String[] patternParts = pattern.split("/");
        String[] pathParts = path.split("/");

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                String key = patternParts[i].substring(1, patternParts[i].length() - 1);
                variables.put(key, pathParts[i]);
            }
        }

        return variables;
    }
}