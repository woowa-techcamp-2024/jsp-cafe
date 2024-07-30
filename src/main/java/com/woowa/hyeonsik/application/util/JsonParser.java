package com.woowa.hyeonsik.application.util;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.Reader;
import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonParser {
    private JsonParser() {}

    /**
     * JSON 데이터가 담긴 Reader를 받아 Map 형태로 파싱하여 반환합니다.
     *
     * <pre>
     * {@Code
     * Map<String, Object> parse = JsonParser.parse(servletRequest.getReader());
     * parse.get("inputData");
     * }
     * </pre>
     *
     * @param stream JSON 형태를 담은 Stream
     * @return Json 데이터를 Key-Value 형태로 담은 Map
     * @throws IllegalArgumentException JSON 포맷이 잘못된 경우
     */
    public static Map<String, Object> parse(Reader stream) {
        try {
            Map<String, Object> rawMap = new JSONParser(stream).parseObject();
            return convertValues(rawMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("JSON 포맷이 잘못되었습니다.", e);
        }
    }

    private static Map<String, Object> convertValues(Map<String, Object> rawMap) {
        return rawMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> convertValue(entry.getValue())
                ));
    }

    private static Object convertValue(Object value) {
        if (value instanceof BigInteger) {  // FIXME BigInt 사용하지 않도록
            BigInteger bigInt = (BigInteger) value;
            if (bigInt.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0
                    && bigInt.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) >= 0) {
                return bigInt.intValue();
            } else {
                return bigInt.longValue();
            }
        } else if (value instanceof Map) {
            return convertValues((Map<String, Object>) value);
        }
        return value;
    }
}