package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.dto.UserUpdateRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@DisplayName("UserUpdateServlet 테스트")
class UserUpdateServletTest {

	@InjectMocks
	private UserUpdateServlet userUpdateServlet;

	@Mock
	private UserService userService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	@Mock
	private RequestDispatcher requestDispatcher;

	private ServletConfig config;

	private ServletContext context;

	@BeforeEach
	void setUp() throws ServletException {
		userUpdateServlet = new UserUpdateServlet();
		userService = mock(UserService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		requestDispatcher = mock(RequestDispatcher.class);

		config = mock(ServletConfig.class);
		context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userService")).thenReturn(userService);
		when(request.getRequestDispatcher("/WEB-INF/user/updateForm.jsp")).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("login")).thenReturn(new Object());

		userUpdateServlet.init(config);
	}

	@Test
	@DisplayName("유효한 유저 업데이트 요청을 처리할 수 있다")
	void doPost_validRequest_updatesUser() throws IOException {
		// given
		UserUpdateRequest dto = new UserUpdateRequest("password", "newName", "newEmail@example.com");
		when(request.getPathInfo()).thenReturn("/1");
		when(request.getParameter("password")).thenReturn("password");
		when(request.getParameter("name")).thenReturn("newName");
		when(request.getParameter("email")).thenReturn("newEmail@example.com");
		when(session.getAttribute("id")).thenReturn("1");

		doNothing().when(userService).updateUser(anyString(), any(UserUpdateRequest.class));

		// when
		userUpdateServlet.doPost(request, response);

		// then
		ArgumentCaptor<UserUpdateRequest> captor = ArgumentCaptor.forClass(UserUpdateRequest.class);
		verify(userService).updateUser(eq("1"), captor.capture());
		UserUpdateRequest capturedRequest = captor.getValue();
		assertThat(capturedRequest.name()).isEqualTo("newName");
		assertThat(capturedRequest.email()).isEqualTo("newEmail@example.com");

		verify(response).sendRedirect("/users");
	}

	@Test
	@DisplayName("존재하지 않는 유저 아이디로 업데이트 요청 시 예외를 던진다")
	void doPost_invalidUserId_sendsError() throws IOException {
		// given
		when(request.getPathInfo()).thenReturn("/1");
		when(request.getParameter("password")).thenReturn("password");
		doThrow(new RuntimeException("User not found")).when(userService).updateUser(eq("1"), any(UserUpdateRequest.class));

		// when & then
		assertThatThrownBy(() -> userUpdateServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("User not found");
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
	@DisplayName("로그인되지 않은 경우 예외를 던진다")
	void doPost_notLogin() throws IOException {
		// given
		when(session.getAttribute("login")).thenReturn(null);

		// when
		userUpdateServlet.doPost(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Test
	@DisplayName("id가 일치하지 않을 때 실패한다")
	void doPost_invalidId() throws IOException {
		// given
		when(session.getAttribute("id")).thenReturn("123");
		when(request.getPathInfo()).thenReturn("/1");
		when(request.getParameter("password")).thenReturn("password");
		doThrow(new RuntimeException("forbidden")).when(userService).updateUser(eq("1"), any(UserUpdateRequest.class));

		// when & then
		assertThatThrownBy(() -> userUpdateServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("forbidden");
	}

	@Test
	@DisplayName("pw가 일치하지 않을 때 실패한다")
	void doPost_invalidPw() throws IOException {
		// given
		when(session.getAttribute("id")).thenReturn("1");
		when(request.getPathInfo()).thenReturn("/1");
		doThrow(new RuntimeException("bad request")).when(userService).updateUser(eq("1"), any(UserUpdateRequest.class));
		when(request.getParameter("password")).thenReturn("wrongPassword");

		// when & then
		assertThatThrownBy(() -> userUpdateServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("bad request");
	}
}
