package com.example.servlet;

import com.example.db.UserDatabase;
import com.example.entity.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserUpdateServlet 테스트")
class UserUpdateServletTest {

	private UserUpdateServlet userUpdateServlet;
	private UserDatabase userDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;
	private HttpSession session;

	@BeforeEach
	void setUp() throws ServletException {
		userUpdateServlet = new UserUpdateServlet();
		userDatabase = mock(UserDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);
		session = mock(HttpSession.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userDatabase);
		when(request.getRequestDispatcher("/user/updateForm.jsp")).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("login")).thenReturn(new Object());

		userUpdateServlet.init(config);
	}

	@Test
	@DisplayName("유효한 유저 업데이트 요청을 처리할 수 있다")
	void doPost_validRequest_updatesUser() throws IOException {
		// given
		User user = new User("1", "password", "name", "email@example.com");
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById("1")).thenReturn(Optional.of(user));
		when(request.getParameter("password")).thenReturn("password");
		when(request.getParameter("name")).thenReturn("newName");
		when(request.getParameter("email")).thenReturn("newEmail@example.com");
		when(session.getAttribute("id")).thenReturn("1");

		// when
		userUpdateServlet.doPost(request, response);

		// then
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userDatabase).update(eq("1"), userCaptor.capture());
		User updatedUser = userCaptor.getValue();
		assertThat(updatedUser.name()).isEqualTo("newName");
		assertThat(updatedUser.email()).isEqualTo("newEmail@example.com");

		verify(response).sendRedirect("/users");
	}

	@Test
	@DisplayName("존재하지 않는 유저 아이디로 업데이트 요청 시 예외를 던진다")
	void doPost_invalidUserId_sendsError() throws IOException {
		// given
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById("1")).thenReturn(Optional.empty());

		// when
		userUpdateServlet.doPost(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	@Test
	@DisplayName("GET 요청을 처리할 수 있다")
	void doGet_displaysUpdateForm() throws ServletException, IOException {
		// given
		when(request.getPathInfo()).thenReturn("/1");

		// when
		userUpdateServlet.doGet(request, response);

		// then
		verify(request).setAttribute("id", "1");
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("login이 되지 않았을 때, 실패한다.")
	void doPost_loginError() throws ServletException, IOException {
		when(session.getAttribute(anyString())).thenReturn(null);

		userUpdateServlet.doPost(request, response);

		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Test
	@DisplayName("id가 일치하지 않을 때 실패한다.")
	void doPost_invalidId() throws ServletException, IOException {
		when(session.getAttribute("id")).thenReturn("123");
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById(anyString())).thenReturn(Optional.of(new User("1", "1", "1", "1")));

		userUpdateServlet.doPost(request, response);

		verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	@Test
	@DisplayName("pw가 일치하지 않을 때 실패한다.")
	void doPost_invalidPw() throws ServletException, IOException {
		when(session.getAttribute("id")).thenReturn("1");
		when(request.getPathInfo()).thenReturn("/1");
		when(userDatabase.findById(anyString())).thenReturn(Optional.of(new User("1", "1", "1", "1")));
		when(request.getParameter("password")).thenReturn("alsjdflkasjdf");

		userUpdateServlet.doPost(request, response);

		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
}
