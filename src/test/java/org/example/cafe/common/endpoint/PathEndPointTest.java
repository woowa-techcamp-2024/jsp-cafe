package org.example.cafe.common.endpoint;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class PathEndPointTest {

    // match returns true when path, method, and query match exactly
    @Test
    void test_match_returns_true_when_path_method_query_match_exactly() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/path");
        when(request.getMethod()).thenReturn("GET");
        when(request.getQueryString()).thenReturn("param=value");

        PathEndPoint endPoint = new PathEndPoint("GET", "/test/path", "param=value");
        boolean result = endPoint.match(request);

        assertTrue(result);
    }

    // match returns true when path ends with '/' and matches the request path prefix
    @Test
    public void test_match_returns_true_when_path_ends_with_slash_and_matches_request_path_prefix() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/path");
        when(request.getMethod()).thenReturn("GET");
        when(request.getQueryString()).thenReturn("param=value");

        PathEndPoint endPoint = new PathEndPoint("GET", "/test/", "param=value");
        boolean result = endPoint.match(request);

        assertTrue(result);
    }

    // match returns false when query does not match request query
    @Test
    public void test_match_returns_false_when_query_does_not_match_request_query() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/path");
        when(request.getMethod()).thenReturn("GET");
        when(request.getQueryString()).thenReturn("different_param=value");

        PathEndPoint endPoint = new PathEndPoint("GET", "/test/path", "param=value");
        boolean result = endPoint.match(request);

        assertFalse(result);
    }
}