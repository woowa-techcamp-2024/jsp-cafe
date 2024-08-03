package com.jspcafe.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jspcafe.test_util.StubHttpServletRequest;
import com.jspcafe.test_util.StubHttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorControllerTest {

  private ErrorController errorController;
  private StubHttpServletRequest request;
  private StubHttpServletResponse response;

  @BeforeEach
  void setUp() {
    errorController = new ErrorController();
    request = new StubHttpServletRequest();
    response = new StubHttpServletResponse();
  }

  @Test
  void 에러_403페이지를_반환한다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/403");

    // When
    errorController.doGet(request, response);

    // Then
    assertEquals(403, response.getStatus());
    assertEquals("/WEB-INF/views/error/error.jsp", request.getForwardedPath());
  }

  @Test
  void 에러_404페이지를_반환한다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/404");

    // When
    errorController.doGet(request, response);

    // Then
    assertEquals(404, response.getStatus());
    assertEquals("/WEB-INF/views/error/error.jsp", request.getForwardedPath());
  }

  @Test
  void 에러_500페이지를_반환한다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/500");

    // When
    errorController.doGet(request, response);

    // Then
    assertEquals(500, response.getStatus());
    assertEquals("/WEB-INF/views/error/error.jsp", request.getForwardedPath());
  }

  @Test
  void 기타_에러_페이지를_반환한다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/405");

    // When
    errorController.doGet(request, response);

    // Then
    assertEquals(405, response.getStatus());
    assertEquals("/WEB-INF/views/error/error.jsp", request.getForwardedPath());
  }
}
