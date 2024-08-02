package org.example.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UrlMatcherTest {

    @Test
    @DisplayName("동일한 패턴과 경로가 주어졌을 때, match 메서드는 true를 리턴한다.")
    public void test_matching_pattern_and_path_with_identical_structure() {
        String pattern = "/home/user";
        String path = "/home/user";
        boolean result = UrlMatcher.match(pattern, path);
        assertTrue(result);
    }

    @Test
    @DisplayName("placeholders와 matching path가 주어졌을 때 extractPathVariables 메서드는 변수를 파싱한 맵을 반환한다.")
    public void test_extracting_variables_from_pattern_with_placeholders() {
        String pattern = "/user/{id}/profile";
        String path = "/user/123/profile";
        Map<String, String> variables = UrlMatcher.extractPathVariables(pattern, path);
        assertEquals("123", variables.get("id"));
    }

    @Test
    @DisplayName("placeholders와 matching되는 path가 주어졌을 때 match 메서드는 true를 반환한다..")
    public void test_matching_pattern_with_placeholders_to_corresponding_path() {
        String pattern = "/user/{id}/profile";
        String path = "/user/123/profile";
        boolean result = UrlMatcher.match(pattern, path);
        assertTrue(result);
    }

    @Test
    @DisplayName("여러개의 placeholders와 matching path가 주어졌을 때 extractPathVariables 메서드는 변수를 파싱한 맵을 반환한다.")
    public void test_extracting_variables_when_all_placeholders_are_present_in_path() {
        String pattern = "/user/{id}/post/{postId}";
        String path = "/user/123/post/456";
        Map<String, String> variables = UrlMatcher.extractPathVariables(pattern, path);
        assertEquals("123", variables.get("id"));
        assertEquals("456", variables.get("postId"));
    }

    @Test
    @DisplayName("pattern과 길이가 다른 path가 주어졌을 때 match 메서드는 false를 반환한다..")
    public void test_pattern_and_path_with_different_lengths() {
        String pattern = "/home/user";
        String path = "/home/user/profile";
        boolean result = UrlMatcher.match(pattern, path);
        assertFalse(result);
    }

    @Test
    @DisplayName("placeholders가 있는 pattern과 해당 부분이 없는 path가 주어졌을 때 match 메서드는 false를 반환한다..")
    public void test_pattern_with_placeholders_but_path_missing_corresponding_segments() {
        String pattern = "/user/{id}/profile";
        String path = "/user/123";
        boolean result = UrlMatcher.match(pattern, path);
        assertFalse(result);
    }

    @Test
    @DisplayName("pattern 내부 placeholder에 특수문자가 있더라도, extractPathVariables 메서드는 변수를 파싱한 맵을 반환한다")
    public void test_pattern_with_special_characters_in_placeholders() {
        String pattern = "/user/{id!@#}/profile";
        String path = "/user/123/profile";
        Map<String, String> variables = UrlMatcher.extractPathVariables(pattern, path);
        assertEquals("123", variables.get("id!@#"));
    }

    @Test
    @DisplayName("경로에 특수문자가 있는 경우에도 매칭된다면 match 메서드는 true를 반환한다.")
    public void test_path_with_special_characters_in_segments() {
        String pattern = "/user/{id}/profile";
        String path = "/user/123!@#/profile";
        boolean result = UrlMatcher.match(pattern, path);
        assertTrue(result);
    }

    @Test
    @DisplayName("경로나 패턴 둘 중 하나가 공백이면, match 메서드는 false를 반환한다.")
    public void test_empty_pattern_or_path() {
        String pattern = "";
        String path = "";
        boolean result = UrlMatcher.match(pattern, path);
        assertTrue(result);

        pattern = "/user/{id}";
        path = "";
        result = UrlMatcher.match(pattern, path);
        assertFalse(result);

        pattern = "";
        path = "/user/123";
        result = UrlMatcher.match(pattern, path);
        assertFalse(result);
    }
}