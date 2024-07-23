package com.example.context;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.db.UserDatabase;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;

public class ApplicationInitializerTest {

	@Test
	@DisplayName("contextInitialized는 UserDatabase를 ServletContext에 설정한다")
	void contextInitialized() {
		// given
		ApplicationInitializer initializer = new ApplicationInitializer();
		ServletContext servletContext = mock(ServletContext.class);
		ServletContextEvent event = new ServletContextEvent(servletContext);

		// when
		initializer.contextInitialized(event);

		// then
		verify(servletContext).setAttribute(eq("userDatabase"), any(UserDatabase.class));
	}
}
