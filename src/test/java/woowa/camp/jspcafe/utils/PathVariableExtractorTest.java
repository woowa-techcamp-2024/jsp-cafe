package woowa.camp.jspcafe.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PathVariableExtractorTest {

    @Test
    @DisplayName("[Success] PathVariable 패턴과 일치하는 경로면 PathVariable 을 추출한다")
    void testSimplePathVariable() {
        String urlPattern = "/users/{userId}";
        String actualPath = "/users/123";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertEquals(1, result.size());
        assertEquals("123", result.get("userId"));
    }

    @Test
    @DisplayName("[Success] PathVariable 패턴과 일치하는 경로면 PathVariable 을 여러개 추출한다")
    void testMultiplePathVariables() {
        String urlPattern = "/users/{userId}/posts/{postId}";
        String actualPath = "/users/123/posts/456";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        System.out.println("result = " + result);
        assertEquals(2, result.size());
        assertEquals("123", result.get("userId"));
        assertEquals("456", result.get("postId"));
    }

    @Test
    @DisplayName("[Success] PathVariable 패턴이 아니면 PathVariable 을 추출하지 않는다")
    void testNoPathVariables() {
        String urlPattern = "/users/all";
        String actualPath = "/users/all";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("[Success] PathVariable 패턴과 요청 경로가 일치하지 않으면 PathVariable 을 추출하지 않는다")
    void testMismatchedLength() {
        String urlPattern = "/users/{userId}";
        String actualPath = "/users/123/extra";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("[Success] Path 가 없으면 PathVariable 을 추출하지 않는다")
    void testEmptyPaths() {
        String urlPattern = "";
        String actualPath = "";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("[Success] 기본 경로면 PathVariable 을 추출하지 않는다")
    void testDefaultPaths() {
        String urlPattern = "/";
        String actualPath = "/";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPatterns")
    @DisplayName("[Success] PathVariable 패턴이 부정확하면 PathVariable 을 추출하지 않는다")
    void testInvalidPatterns(String urlPattern, String actualPath) {
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertTrue(result.isEmpty());
    }

    private static Stream<Arguments> provideInvalidPatterns() {
        return Stream.of(
                Arguments.of("/users/{userId", "/users/123"),  // 미완성된 중괄호
                Arguments.of("/users/userId}", "/users/123"),  // 미완성된 중괄호
                Arguments.of("/users/{}", "/users/123"),       // 빈 변수명
                Arguments.of("/users/{ userId }", "/users/123") // 공백이 있는 변수명
        );
    }

    @Test
    @DisplayName("[Success] Path Variable 에 특수문자가 포함되도 추출에 성공한다")
    void testPathVariableWithSpecialCharacters() {
        String urlPattern = "/users/{userId}";
        String actualPath = "/users/user@example.com";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertEquals(1, result.size());
        assertEquals("user@example.com", result.get("userId"));
    }

    @Test
    @DisplayName("[Success] Path Variable 패턴에 대문자가 들어가도 추출에 성공한다")
    void testCaseInsensitivity() {
        String urlPattern = "/Users/{UserId}";
        String actualPath = "/users/123";
        Map<String, String> result = PathVariableExtractor.extractPathVariables(urlPattern, actualPath);
        assertEquals("123", result.get("UserId"));
    }

}