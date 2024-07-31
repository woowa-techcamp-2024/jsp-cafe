package org.example.util;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static Map<String, Object> parse(String jsonString) {
        Map<String, Object> result = new HashMap<>();
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
            String[] keyValuePairs = jsonString.split(",");
            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":", 2);
                if (entry.length == 2) {
                    String key = entry[0].trim().replace("\"", "");
                    String value = entry[1].trim();
                    result.put(key, parseValue(value));
                }
            }
        }
        return result;
    }

    private static Object parseValue(String value) {
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else if (value.equals("null")) {
            return null;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException e2) {
                    return value;
                }
            }
        }
    }
}
