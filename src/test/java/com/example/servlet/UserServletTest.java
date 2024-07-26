package com.example.servlet;

import com.example.dto.SignupRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserServlet 테스트")
class UserServletTest {

	private UserServlet userServlet;
	private UserService userService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;
	private static MockedStatic<DtoCreationUtil> mockedDtoCreationUtil;

	@BeforeEach
	void setUp() throws ServletException {
		userServlet = new UserServlet();
		userService = mock(UserService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		mockedDtoCreationUtil = mockStatic(DtoCreationUtil.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("userService")).thenReturn(userService);
		when(request.getRequestDispatcher("/user/list.jsp")).thenReturn(requestDispatcher);

		userServlet.init(config);
	}

	@AfterEach
	void tearDown() {
		mockedDtoCreationUtil.close();
	}

	@Test
	@DisplayName("유효한 유저 생성 요청을 처리할 수 있다")
	void doPost_validRequest_createsUser() throws IOException {
		// given
		SignupRequest signupRequest = new SignupRequest("1", "password056", "name", "email@example.com");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(SignupRequest.class), any(HttpServletRequest.class)))
			.thenReturn(signupRequest);

		// when
		userServlet.doPost(request, response);

		// then
		verify(userService, times(1)).signup(signupRequest);
		verify(response).sendRedirect("/users");
	}

	@Test
	@DisplayName("필수 필드가 누락된 경우 예외를 던진다")
	void doPost_missingFields_sendsError() throws IOException {
		// given
		SignupRequest signupRequest = new SignupRequest("", "password056", "", "");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(SignupRequest.class), any(HttpServletRequest.class)))
			.thenReturn(signupRequest);

		// when
		assertThatThrownBy(() -> userServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class);
	}
}
