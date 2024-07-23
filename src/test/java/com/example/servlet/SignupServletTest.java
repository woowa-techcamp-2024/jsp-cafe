package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.example.db.UserDatabase;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignupServletTest {

	private SignupServlet signupServlet;
	private UserDatabase userDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@BeforeEach
	void setUp() throws ServletException {
		signupServlet = new SignupServlet();
		userDatabase = mock(UserDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userDatabase")).thenReturn(userDatabase);

		signupServlet.init(config);
	}

	@Test
	@DisplayName("회원가입 요청을 처리할 수 있다")
	void doPost_validRequest_createsUser() throws IOException {
		when(request.getParameter("userId")).thenReturn("1");
		when(request.getParameter("password")).thenReturn("password");
		when(request.getParameter("name")).thenReturn("name");
		when(request.getParameter("email")).thenReturn("email@example.com");

		signupServlet.doPost(request, response);

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userDatabase).insert(userCaptor.capture());
		User insertedUser = userCaptor.getValue();
		assertThat(insertedUser.id()).isEqualTo("1");
		assertThat(insertedUser.password()).isEqualTo("password");
		assertThat(insertedUser.name()).isEqualTo("name");
		assertThat(insertedUser.email()).isEqualTo("email@example.com");

		verify(response).sendRedirect("/user/list.jsp");
	}

	@Test
	@DisplayName("필수 필드가 누락된 경우 예외를 던진다")
	void doPost_missingFields_throwsException() {
		when(request.getParameter("userId")).thenReturn(null);

		assertThatThrownBy(() -> signupServlet.doPost(request, response))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("All fields are required");
	}

	@Test
	@DisplayName("GET 요청을 처리할 수 있다")
	void doGet_redirectsToListPage() throws IOException {
		signupServlet.doGet(request, response);

		verify(response).sendRedirect("/user/list.jsp");
	}
}
