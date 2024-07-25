package com.hyeonuk.jspcafe.utils;
    import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class YamlParser {
    public Map<String, Object> parse(String filename) {
        Map<String, Object> result = new HashMap<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            String line;
            Map<String, Object> currentMap = result;
            int currentIndent = 0;
            Map<Integer, Map<String, Object>> indentMap = new HashMap<>();
            indentMap.put(0, result);

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\\s+$", ""); // 오른쪽 공백 제거
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue; // 빈 줄이나 주석 무시
                }

                int indent = countLeadingSpaces(line);
                line = line.trim();
                String[] parts = line.split(":", 2);

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    if (value.isEmpty()) {
                        // 새로운 중첩 맵 시작
                        Map<String, Object> nestedMap = new HashMap<>();
                        currentMap.put(key, nestedMap);
                        currentMap = nestedMap;
                        currentIndent = indent;
                        indentMap.put(indent, currentMap);
                    } else {
                        // 값 제거
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        currentMap.put(key, value);
                    }
                }

                // 들여쓰기 수준이 감소하면 상위 맵으로 이동
                if (indent < currentIndent) {
                    currentMap = indentMap.get(indent);
                    currentIndent = indent;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(filename+" 이 존재하지 않습니다.");
        }
        return result;
    }

    private int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }
}
