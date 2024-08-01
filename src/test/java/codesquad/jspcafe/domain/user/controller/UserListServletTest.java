package codesquad.jspcafe.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import codesquad.jspcafe.common.MockTemplate;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("UserListServlet은")
class UserListServletTest extends MockTemplate {

    @InjectMocks
    private UserListServlet userListServlet;

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
        userListServlet.init(config);
        // Assert
        assertThat(userListServlet)
            .extracting("userService")
            .isEqualTo(userService);
    }

    @Test
    @DisplayName("/users GET 요청을 처리하여 userList.jsp 페이지로 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        List<UserCommonResponse> userList = List.of();
        given(userService.findAllUser()).willReturn(userList);
        given(request.getRequestDispatcher("/WEB-INF/jsp/userList.jsp")).willReturn(
            requestDispatcher);
        // Act
        userListServlet.doGet(request, response);
        // Assert
        verify(request).setAttribute("userList", userList);
        verify(requestDispatcher).forward(request, response);
    }
}