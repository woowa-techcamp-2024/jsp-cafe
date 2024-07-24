package com.jspcafe.util;

import com.jspcafe.test_util.StubHttpServletRequest;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpUtilsTest {
    @Test
    void HttpRequest_body_내용을_정상적으로_반환한다() throws IOException {
        // Given
        String expectedBody = "This is the request body";
        StubHttpServletRequest request = new StubHttpServletRequest();
        request.setBody(expectedBody);

        // When
        String actualBody = HttpUtils.readRequestBody(request);

        // Then
        assertEquals(expectedBody, actualBody);
    }

    @Test
    void JSON_형식의_요청_본문을_Map으로_변환한다() throws IOException {
        // Given
        String jsonBody = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\"}";
        StubHttpServletRequest request = new StubHttpServletRequest();
        request.setBody(jsonBody);

        // When
        Map<String, Object> result = HttpUtils.getJsonRequestBody(request);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("John Doe", result.get("name"));
        assertEquals(30, result.get("age"));
        assertEquals("New York", result.get("city"));
    }
}
