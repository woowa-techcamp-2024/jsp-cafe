package com.example.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import com.example.exception.BaseException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ExceptionFilter 테스트")
class ExceptionFilterTest {

	private ExceptionFilter exceptionFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;

	@BeforeEach
	void setUp() {
		exceptionFilter = new ExceptionFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		filterChain = mock(FilterChain.class);
	}

	@Test
	@DisplayName("BaseException이 발생한 경우 적절한 에러 페이지로 포워딩한다")
	void testBaseException() throws IOException, ServletException {
		BaseException baseException = new BaseException(400, "Base exception");
		doThrow(baseException).when(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(request.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

		exceptionFilter.doFilter(request, response, filterChain);

		verify(request).setAttribute("statusCode", 400);
		verify(request).setAttribute("message", "Base exception");
		verify(response).setStatus(400);
		verify(dispatcher).forward(request, response);
	}

	@Test
	@DisplayName("Generic Exception이 발생한 경우 적절한 에러 페이지로 포워딩한다")
	void testGenericException() throws IOException, ServletException {
		RuntimeException exception = new RuntimeException("Generic exception");
		doThrow(exception).when(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(request.getRequestDispatcher("/WEB-INF/error.jsp")).thenReturn(dispatcher);

		exceptionFilter.doFilter(request, response, filterChain);

		verify(request).setAttribute("statusCode", 500);
		verify(request).setAttribute("message", "Internal Server Error");
		verify(response).setStatus(500);
		verify(dispatcher).forward(request, response);
	}
}
