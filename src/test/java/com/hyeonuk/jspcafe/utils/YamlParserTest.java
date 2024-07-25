package com.hyeonuk.jspcafe.utils;

import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YamlParser 테스트")
class YamlParserTest {

    @Nested
    @DisplayName("parse 메소드")
    class ParseMethod {

        @Test
        @DisplayName("기본 YAML 파일 파싱")
        void parseBasicYaml() {
            YamlParser parser = new YamlParser();
            Yaml result = parser.parse("test_basic.yml");

            assertEquals("value1", result.get("key1"));
            assertEquals("value2", result.get("key2"));
        }

        @Test
        @DisplayName("중첩된 YAML 구조 파싱")
        void parseNestedYaml() {
            YamlParser parser = new YamlParser();
            Yaml result = parser.parse("test_nested.yml");

            assertTrue(result.get("parent") instanceof Map);
            Map<String, Object> parent = result.getMap("parent");
            assertEquals("value1", parent.get("child1"));
            assertEquals("value2", parent.get("child2"));
        }

        @Test
        @DisplayName("빈 줄과 주석이 있는 YAML 파싱")
        void parseYamlWithEmptyLinesAndComments() {
            YamlParser parser = new YamlParser();
            Yaml result = parser.parse("test_comments.yml");

            assertEquals("value1", result.get("key1"));
            assertEquals("value2", result.get("key2"));
        }

        @Test
        @DisplayName("따옴표로 둘러싸인 값 파싱")
        void parseQuotedValues() {
            YamlParser parser = new YamlParser();
            Yaml result = parser.parse("test_quoted.yml");

            assertEquals("quoted value", result.get("key1"));
            assertEquals("normal value", result.get("key2"));
        }

        @Test
        @DisplayName("들여쓰기 수준 변경 처리")
        void parseChangingIndentationLevels() {
            YamlParser parser = new YamlParser();
            Yaml result = parser.parse("test_indentation.yml");

            assertTrue(result.get("level1") instanceof Map);
            Map<String, Object> level1 = result.getMap("level1");
            assertTrue(level1.get("level2") instanceof Map);
            Map<String, Object> level2 = (Map<String, Object>) level1.get("level2");
            assertEquals("value3", level2.get("level3"));
            assertEquals("value2b", level1.get("level2b"));
            assertEquals("value1b", result.get("level1b"));
        }

        @Test
        @DisplayName("존재하지 않는 파일 파싱 시 예외 발생")
        void parseNonExistentFile() {
            YamlParser parser = new YamlParser();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> parser.parse("non_existent.yml"));
            assertEquals("non_existent.yml 이 존재하지 않습니다.", exception.getMessage());
        }
    }
}