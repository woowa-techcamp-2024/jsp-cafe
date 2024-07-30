package com.woowa.hyeonsik.application.util;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.Reader;
import java.util.Map;

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
            return new JSONParser(stream).parseObject();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("JSON 포맷이 잘못되었습니다.", e);
        }
    }
}
