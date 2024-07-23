package com.example.servlet;

import com.example.db.UserMemoryDatabase;
import com.example.entity.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("UserProfileServlet 테스트")
class UserProfileServletTest {

	private UserProfileServlet userProfileServlet;
	private UserMemoryDatabase userMemoryDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;

	@BeforeEach
	void setUp() throws ServletException {
		userProfileServlet = new UserProfileServlet();
		userMemoryDatabase = mock(UserMemoryDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userMemoryDatabase);
		when(request.getRequestDispatcher("/user/profile.jsp")).thenReturn(requestDispatcher);

		userProfileServlet.init(config);
	}

	@Test
	@DisplayName("유효한 유저 아이디로 GET 요청을 처리할 수 있다")
	void doGet_validUserId_displaysUserProfile() throws ServletException, IOException {
		// given
		User user = new User("1", "password", "name", "email@example.com");
		when(request.getPathInfo()).thenReturn("/1");
		when(userMemoryDatabase.findById("1")).thenReturn(Optional.of(user));

		// when
		userProfileServlet.doGet(request, response);

		// then
		verify(request).setAttribute("user", user);
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("존재하지 않는 유저 아이디로 GET 요청을 처리할 때 예외를 던진다")
	void doGet_invalidUserId_throwsException() {
		// given
		when(request.getPathInfo()).thenReturn("/1");
		when(userMemoryDatabase.findById("1")).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userProfileServlet.doGet(request, response))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("User not found");
	}
}
