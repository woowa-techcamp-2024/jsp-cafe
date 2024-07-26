package com.hyeonuk.jspcafe.utils;

import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class YamlParser {
    public Yaml parse(String filename) {
        Map<String, Object> result = new HashMap<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {
            assert input != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

                String line;
                Deque<Map<String, Object>> mapStack = new LinkedList<>();
                mapStack.push(result);
                int previousIndent = -1;
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll("\\s+$", ""); // 오른쪽 공백 제거
                    if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                        continue; // 빈 줄이나 주석 무시
                    }

                    int indent = countLeadingSpaces(line);
                    if(previousIndent > indent){
                        mapStack.pollLast();
                    }
                    previousIndent = indent;
                    line = line.trim();
                    String[] parts = line.split(":", 2);

                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        Map<String,Object> currentMap = mapStack.peekLast();
                        if (value.isEmpty()) {
                            // new nested map
                            Map<String, Object> nestedMap = new HashMap<>();
                            currentMap.put(key, nestedMap);
                            mapStack.offerLast(nestedMap);
                        } else { //
                            // remove double quotes or quotes
                            if (value.startsWith("\"") && value.endsWith("\"")
                                    || value.startsWith("'") && value.endsWith("'") ){
                                value = value.substring(1, value.length() - 1);
                            }
                            currentMap.put(key,value);
                        }
                    }
                }
                return new Yaml(result, filename);
            }
        } catch (IOException | AssertionError e) {
            throw new IllegalArgumentException(filename + " 이 존재하지 않습니다.");
        }
    }

    int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }
}