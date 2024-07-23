package com.example.context;

import com.example.db.ArticleMemoryDatabase;
import com.example.db.UserMemoryDatabase;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("userDatabase", new UserMemoryDatabase());
		sce.getServletContext().setAttribute("articleDatabase", new ArticleMemoryDatabase());
	}
}
