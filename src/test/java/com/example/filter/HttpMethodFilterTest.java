package com.example.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpMethodFilter 테스트")
class HttpMethodFilterTest {

	private HttpMethodFilter httpMethodFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;

	@BeforeEach
	void setUp() {
		httpMethodFilter = new HttpMethodFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		filterChain = mock(FilterChain.class);
	}

	@Test
	@DisplayName("PUT 메소드 요청을 처리할 수 있다")
	void testPutMethod() throws IOException, ServletException {
		when(request.getParameter("_method")).thenReturn("PUT");

		httpMethodFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(any(HttpServletRequestWrapper.class), eq(response));
	}

	@Test
	@DisplayName("DELETE 메소드 요청을 처리할 수 있다")
	void testDeleteMethod() throws IOException, ServletException {
		when(request.getParameter("_method")).thenReturn("DELETE");

		httpMethodFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(any(HttpServletRequestWrapper.class), eq(response));
	}

	@Test
	@DisplayName("기타 메소드 요청을 처리할 수 있다")
	void testOtherMethod() throws IOException, ServletException {
		when(request.getParameter("_method")).thenReturn("POST");

		httpMethodFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(eq(request), eq(response));
	}
}
