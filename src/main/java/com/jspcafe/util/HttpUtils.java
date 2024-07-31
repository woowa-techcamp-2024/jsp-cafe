package com.jspcafe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private HttpUtils() {
  }

  public static String readRequestBody(final HttpServletRequest request) throws IOException {
    StringBuilder buffer = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }
    }
    return buffer.toString();
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> getJsonRequestBody(final HttpServletRequest request)
      throws IOException {
    String requestBody = readRequestBody(request);
    return objectMapper.readValue(requestBody, Map.class);
  }
}
