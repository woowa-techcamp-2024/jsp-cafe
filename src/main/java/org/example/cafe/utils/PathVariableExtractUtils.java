package org.example.cafe.utils;

public final class PathVariableExtractUtils {

    private PathVariableExtractUtils() {
    }

    public static <T> T extractAsteriskValue(String path, int index, Class<T> type) {
        String[] pathParts = path.split("/");
        String value = pathParts[index];

        if (type == Long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (type == String.class) {
            return (T) value;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
    }
}
