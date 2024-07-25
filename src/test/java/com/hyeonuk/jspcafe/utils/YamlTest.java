package com.hyeonuk.jspcafe.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Yaml 클래스 테스트")
class YamlTest {

    private Yaml yaml;
    private final String testPath = "test.yml";

    @BeforeEach
    void setUp() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");

        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("nestedKey1", "nestedValue1");
        nestedMap.put("nestedKey2", "nestedValue2");
        testMap.put("nested", nestedMap);

        testMap.put("emptyString", "");

        yaml = new Yaml(testMap, testPath);
    }

    @Nested
    @DisplayName("get 메소드 테스트")
    class GetMethodTest {

        @Test
        @DisplayName("단일 키로 값 가져오기")
        void getSimpleKey() {
            assertEquals("value1", yaml.get("key1"));
            assertEquals("value2", yaml.get("key2"));
        }

        @Test
        @DisplayName("중첩된 키로 값 가져오기")
        void getNestedKey() {
            assertEquals("nestedValue1", yaml.get("nested.nestedKey1"));
            assertEquals("nestedValue2", yaml.get("nested.nestedKey2"));
        }

        @Test
        @DisplayName("존재하지 않는 키로 값 가져오기")
        void getNonExistentKey() {
            assertNull(yaml.get("nonExistentKey"));
            assertNull(yaml.get("nested.nonExistentKey"));
        }

        @Test
        @DisplayName("중간 경로가 Map이 아닌 경우")
        void getWithNonMapIntermediatePath() {
            assertNull(yaml.get("key1.subkey"));
        }
    }

    @Nested
    @DisplayName("getMap 메소드 테스트")
    class GetMapMethodTest {

        @Test
        @DisplayName("맵 가져오기")
        void getExistingMap() {
            Map<String, Object> nestedMap = yaml.getMap("nested");
            assertNotNull(nestedMap);
            assertEquals("nestedValue1", nestedMap.get("nestedKey1"));
            assertEquals("nestedValue2", nestedMap.get("nestedKey2"));
        }

        @Test
        @DisplayName("맵이 아닌 값 가져오기")
        void getNonMapValue() {
            assertNull(yaml.getMap("key1"));
        }

        @Test
        @DisplayName("존재하지 않는 키로 맵 가져오기")
        void getNonExistentMap() {
            assertNull(yaml.getMap("nonExistentMap"));
        }
    }

    @Nested
    @DisplayName("getString 메소드 테스트")
    class GetStringMethodTest {

        @Test
        @DisplayName("문자열 값 가져오기")
        void getExistingString() {
            assertEquals("value1", yaml.getString("key1"));
            assertEquals("value2", yaml.getString("key2"));
        }

        @Test
        @DisplayName("존재하지 않는 키로 문자열 가져오기")
        void getNonExistentString() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> yaml.getString("nonExistentKey"));
            assertEquals(testPath + " 안에 nonExistentKey값이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("맵 값을 문자열로 가져오기 시도")
        void getMapAsString() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> yaml.getString("nested"));
            assertEquals(testPath + " 안에 nested값이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("빈 문자열 가져오기")
        void getEmptyString() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> yaml.getString("emptyString"));
            assertEquals(testPath + " 안에 emptyString값이 존재하지 않습니다.", exception.getMessage());
        }
    }
}
