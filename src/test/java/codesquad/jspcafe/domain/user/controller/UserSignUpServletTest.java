package codesquad.jspcafe.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("UserSignUpServlet은")
class UserSignUpServletTest extends MockTemplate {

    @InjectMocks
    private UserSignUpServlet userSignUpServlet;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("서블릿을 초기화하여 서블릿 컨텍스트에서 UserSerivce를 가져온다.")
    void init() throws ServletException {
        // Arrange
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        given(config.getServletContext()).willReturn(context);
        given(context.getAttribute("userService")).willReturn(userService);
        // Act
        userSignUpServlet.init(config);
        // Assert
        assertThat(userSignUpServlet)
            .extracting("userService")
            .isEqualTo(userService);
    }

    @Test
    @DisplayName("/users/signup GET 요청을 처리하여 회원가입 페이지로 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher("/WEB-INF/jsp/userSignupForm.jsp")).willReturn(
            dispatcher);
        // Act
        userSignUpServlet.doGet(request, response);
        // Assert
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("/users/signup POST 요청을 처리하여 새로운 사용자를 생성한 후 / 페이지로 리디렉션한다.")
    void doPost() throws ServletException, IOException {
        // Arrange
        Map<String, String[]> parameterMap = Map.of(
            "userId", new String[]{"test"},
            "password", new String[]{"test"},
            "name", new String[]{"test"},
            "email", new String[]{"test@gmail.com"}
        );
        given(request.getParameterMap()).willReturn(parameterMap);
        // Act
        userSignUpServlet.doPost(request, response);
        // Assert
        verify(userService).createUser(parameterMap);
        verify(response).sendRedirect("/");
    }
}