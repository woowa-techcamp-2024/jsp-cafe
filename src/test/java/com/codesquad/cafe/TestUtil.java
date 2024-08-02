package com.codesquad.cafe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public final class TestUtil {

    public static final Map<Integer, String> errorMessages = Map.of(
            400, "400 Bad Request",
            401, "401 Unauthorized",
            403, "403 Forbidden",
            404, "404 Not Found",
            405, "405 Method Not Allowed",
            500, "500 Internal Server Error"
    );

    public static void assertErrorPageResponse(HttpResponse response, int errorCode) throws IOException {
        assertEquals(errorCode, response.getStatusLine().getStatusCode());
        assertTrue(response.getEntity().getContentType().getValue().contains("text/html"));
        assertTrue(EntityUtils.toString(response.getEntity()).contains(errorMessages.get(errorCode)));
    }

    public static void assertErrorPageResponse(SavedHttpResponse response, int errorCode) {
        assertEquals(errorCode, response.getStatusLine().getStatusCode());
        assertTrue(response.getContentType().contains("text/html"));
        assertTrue(response.getBody().contains(errorMessages.get(errorCode)));
    }

    public static String getSessionIdFromSetCookieHeader(String setCookieHeader) {
        return setCookieHeader.split(";")[0].split("=")[1];
    }
}
