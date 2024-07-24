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
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserServlet 테스트")
class UserServletTest {

	private UserServlet userServlet;
	private UserMemoryDatabase userMemoryDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;

	@BeforeEach
	void setUp() throws ServletException {
		userServlet = new UserServlet();
		userMemoryDatabase = mock(UserMemoryDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userMemoryDatabase);
		when(request.getRequestDispatcher("/user/list.jsp")).thenReturn(requestDispatcher);

		userServlet.init(config);
	}

	@Test
	@DisplayName("GET 요청을 처리할 수 있다")
	void doGet_displaysUserList() throws ServletException, IOException {
		// given
		User user1 = new User("1", "password1", "name1", "email1@example.com");
		User user2 = new User("2", "password2", "name2", "email2@example.com");
		when(userMemoryDatabase.findAll()).thenReturn(List.of(user1, user2));

		// when
		userServlet.doGet(request, response);

		// then
		verify(request).setAttribute("userList", List.of(user1, user2));
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("유효한 유저 생성 요청을 처리할 수 있다")
	void doPost_validRequest_createsUser() throws IOException {
		// given
		when(request.getParameter("userId")).thenReturn("1");
		when(request.getParameter("password")).thenReturn("password");
		when(request.getParameter("name")).thenReturn("name");
		when(request.getParameter("email")).thenReturn("email@example.com");

		// when
		userServlet.doPost(request, response);

		// then
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userMemoryDatabase).insert(userCaptor.capture());
		User insertedUser = userCaptor.getValue();
		assertThat(insertedUser.id()).isEqualTo("1");
		assertThat(insertedUser.password()).isEqualTo("password");
		assertThat(insertedUser.name()).isEqualTo("name");
		assertThat(insertedUser.email()).isEqualTo("email@example.com");

		verify(response).sendRedirect("/users");
	}

	@Test
	@DisplayName("필수 필드가 누락된 경우 예외를 던진다")
	void doPost_missingFields_sendsError() throws IOException {
		// given
		when(request.getParameter("userId")).thenReturn("");

		// when
		userServlet.doPost(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
}
