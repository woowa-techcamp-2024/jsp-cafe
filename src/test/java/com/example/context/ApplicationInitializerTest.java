package com.example.context;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.db.ArticleMemoryDatabase;
import com.example.db.UserMemoryDatabase;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;

@DisplayName("ApplicationInitializer 테스트")
class ApplicationInitializerTest {

	private ApplicationInitializer initializer;
	private ServletContextEvent event;
	private ServletContext context;

	@BeforeEach
	void setUp() {
		initializer = new ApplicationInitializer();
		context = mock(ServletContext.class);
		event = new ServletContextEvent(context);
	}

	@Test
	@DisplayName("contextInitialized는 UserDatabase와 ArticleDatabase를 ServletContext에 설정한다")
	void contextInitialized() {
		initializer.contextInitialized(event);

		verify(context).setAttribute(eq("userDatabase"), any(UserMemoryDatabase.class));
		verify(context).setAttribute(eq("articleDatabase"), any(ArticleMemoryDatabase.class));
	}
}
