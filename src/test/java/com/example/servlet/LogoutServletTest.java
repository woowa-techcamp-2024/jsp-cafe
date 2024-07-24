package com.example.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.db.UserMemoryDatabase;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutServletTest {

	private LogoutServlet logoutServlet = new LogoutServlet();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	@BeforeEach
	void setUp() throws ServletException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
	}

	@Test
	@DisplayName("로그아웃 시 root로 리다이렉트된다.")
	void doGet_logout() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);

		logoutServlet.doGet(request, response);

		verify(session).invalidate();
		verify(response).sendRedirect(anyString());
	}
}
