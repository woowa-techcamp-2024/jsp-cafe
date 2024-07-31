package com.woowa.hyeonsik.application.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    @Test
    @DisplayName("유효한 JSON 문자열을 파싱하여 Map으로 변환한다")
    void parseValidJson() {
        String validJson = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        StringReader reader = new StringReader(validJson);

        Map<String, Object> result = JsonParser.parse(reader);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("John", result.get("name"));
        assertEquals(30, result.get("age"));
        assertEquals("New York", result.get("city"));
    }

    @Test
    @DisplayName("빈 JSON 객체를 파싱하여 빈 Map으로 변환한다")
    void parseEmptyJson() {
        String emptyJson = "{}";
        StringReader reader = new StringReader(emptyJson);

        Map<String, Object> result = JsonParser.parse(reader);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("중첩된 JSON 객체를 파싱하여 Map으로 변환한다")
    void parseNestedJson() {
        String nestedJson = "{\"person\":{\"name\":\"John\",\"age\":30},\"address\":{\"city\":\"New York\",\"country\":\"USA\"}}";
        StringReader reader = new StringReader(nestedJson);

        Map<String, Object> result = JsonParser.parse(reader);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get("person") instanceof Map);
        assertTrue(result.get("address") instanceof Map);

        Map<String, Object> person = (Map<String, Object>) result.get("person");
        assertEquals("John", person.get("name"));
        assertEquals(30, person.get("age"));

        Map<String, Object> address = (Map<String, Object>) result.get("address");
        assertEquals("New York", address.get("city"));
        assertEquals("USA", address.get("country"));
    }

    @Test
    @DisplayName("유효하지 않은 JSON을 파싱할 때 IllegalArgumentException을 던진다")
    void parseInvalidJson() {
        String invalidJson = "{\"name\":\"John\",\"age\":30,}";  // 쉼표 후 추가 필드 없음
        StringReader reader = new StringReader(invalidJson);

        assertThrows(IllegalArgumentException.class, () -> JsonParser.parse(reader));
    }

    @Test
    @DisplayName("JSON 배열을 파싱할 때 IllegalArgumentException을 던진다")
    void parseJsonArray() {
        String jsonArray = "[1, 2, 3]";
        StringReader reader = new StringReader(jsonArray);

        assertThrows(IllegalArgumentException.class, () -> JsonParser.parse(reader));
    }
}
