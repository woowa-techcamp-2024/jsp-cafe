package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.dto.LoginRequest;
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
import org.mockito.MockedStatic;

public class LoginServletTest {

	private LoginServlet loginServlet;
	private UserService userService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;
	private HttpSession session;
	private static MockedStatic<DtoCreationUtil> mockedDtoCreationUtil;
	private User user = new User("id", "password", "name", "email");

	@BeforeEach
	void setUp() throws ServletException {
		loginServlet = new LoginServlet();
		userService = mock(UserService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);
		session = mock(HttpSession.class);

		mockedDtoCreationUtil = mockStatic(DtoCreationUtil.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userService")).thenReturn(userService);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);

		loginServlet.init(config);
	}

	@AfterEach
	void tearDown() {
		mockedDtoCreationUtil.close();
	}

	@Test
	void doGet() throws IOException, ServletException {
		loginServlet.doGet(request, response);

		verify(requestDispatcher).forward(request, response);
	}

	@Test
	void doPost() throws IOException, ServletException {
		// given
		LoginRequest loginRequest = new LoginRequest("id", "password");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(LoginRequest.class), any(HttpServletRequest.class)))
			.thenReturn(loginRequest);
		when(userService.login(any(LoginRequest.class))).thenReturn(user);

		// when
		loginServlet.doPost(request, response);

		// then
		verify(session).setAttribute("login", true);
		verify(session).setAttribute("name", user.name());
		verify(session).setAttribute("id", user.id());
		verify(response).sendRedirect("/");
	}

	@Test
	void doPost_userEmpty() throws IOException, ServletException {
		// given
		LoginRequest loginRequest = new LoginRequest("id", "password");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(LoginRequest.class), any(HttpServletRequest.class)))
			.thenReturn(loginRequest);
		when(userService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("invalid auth"));

		// when
		assertThatThrownBy(() -> loginServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	void doPost_invalidPassword() throws IOException, ServletException {
		// given
		LoginRequest loginRequest = new LoginRequest("id", "wrongPassword");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(LoginRequest.class), any(HttpServletRequest.class)))
			.thenReturn(loginRequest);
		when(userService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("invalid auth"));

		// when
		assertThatThrownBy(() -> loginServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class);
	}
}
