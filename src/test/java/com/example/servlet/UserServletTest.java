package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.db.UserDatabase;
import com.example.entity.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@DisplayName("UserServlet 테스트")
class UserServletTest {

	private UserServlet userServlet;
	private UserDatabase userDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;

	@BeforeEach
	void setUp() throws ServletException {
		userServlet = new UserServlet();
		userDatabase = mock(UserDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userDatabase);
		when(request.getRequestDispatcher("/user/profile.jsp")).thenReturn(requestDispatcher);

		userServlet.init(config);
	}

	@Test
	@DisplayName("유효한 유저 아이디로 GET 요청을 처리할 수 있다")
	void doGet_validUserId_displaysUserProfile() throws ServletException, IOException {
		User user = new User("1", "password", "name", "email@example.com");
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById("1")).thenReturn(Optional.of(user));

		userServlet.doGet(request, response);

		verify(request).setAttribute("user", user);
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("존재하지 않는 유저 아이디로 GET 요청을 처리할 때 예외를 던진다")
	void doGet_invalidUserId_throwsException() {
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById("1")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userServlet.doGet(request, response))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("User not found");
	}
}
