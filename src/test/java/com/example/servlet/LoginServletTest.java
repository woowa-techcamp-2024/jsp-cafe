package com.example.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.db.UserDatabase;
import com.example.entity.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServletTest {

	private LoginServlet loginServlet;
	private UserDatabase userDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;
	private HttpSession session;
	private User user = new User("id", "password", "name", "email");

	@BeforeEach
	void setUp() throws ServletException {
		loginServlet = new LoginServlet();
		userDatabase = mock(UserDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);
		session = mock(HttpSession.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userDatabase);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

		loginServlet.init(config);
	}

	@Test
	void doGet() throws IOException, ServletException {
		loginServlet.doGet(request, response);

		verify(requestDispatcher).forward(request, response);
	}

	@Test
	void doPost() throws IOException, ServletException {
		when(request.getParameter("id")).thenReturn("id");
		when(request.getParameter("password")).thenReturn("password");
		when(userDatabase.findById(anyString())).thenReturn(Optional.of(user));
		when(request.getSession()).thenReturn(session);

		loginServlet.doPost(request, response);

		verify(session).setAttribute("login", true);
		verify(session).setAttribute("name", user.name());
		verify(session).setAttribute("id", user.id());
		verify(response).sendRedirect("/");
	}

	@Test
	void doPost_userEmpty() throws IOException, ServletException {
		when(request.getParameter("id")).thenReturn("id");
		when(userDatabase.findById(anyString())).thenReturn(Optional.empty());

		loginServlet.doPost(request, response);

		verify(requestDispatcher).forward(request, response);
	}

	@Test
	void doPost_invalidPassword() throws IOException, ServletException {
		when(request.getParameter("id")).thenReturn("id");
		when(request.getParameter("password")).thenReturn("password1");
		when(userDatabase.findById(anyString())).thenReturn(Optional.of(user));

		loginServlet.doPost(request, response);

		verify(requestDispatcher).forward(request, response);
	}
}
