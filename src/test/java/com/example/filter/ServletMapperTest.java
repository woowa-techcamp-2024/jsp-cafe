package com.example.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServletMapper 테스트")
class ServletMapperTest {

	private ServletContext servletContext;
	private ServletRegistration servletRegistration;

	@BeforeEach
	void setUp() {
		servletContext = mock(ServletContext.class);
		servletRegistration = mock(ServletRegistration.class);
	}

	@Test
	@DisplayName("서블릿 매핑을 초기화할 수 있다")
	void testInit() {
		// when(servletContext.getServletRegistrations()).thenReturn(Map.of("servlet", servletRegistration));
		when(servletRegistration.getMappings()).thenReturn(Collections.singleton("/example/*"));

		ServletMapper.init(servletContext);

		String className = ServletMapper.getServletClassName("/example/test");
		assertThat(className).isEqualTo(servletRegistration.getClassName());
	}

	@Test
	@DisplayName("서블릿 경로에 매핑된 클래스 이름을 반환할 수 있다")
	void testGetServletClassName() {
		// when(servletContext.getServletRegistrations()).thenReturn(Map.of("servlet", servletRegistration));
		when(servletRegistration.getMappings()).thenReturn(Collections.singleton("/example/*"));
		when(servletRegistration.getClassName()).thenReturn("com.example.ExampleServlet");

		ServletMapper.init(servletContext);

		String className = ServletMapper.getServletClassName("/example/test");
		assertThat(className).isEqualTo(null);
	}
}
