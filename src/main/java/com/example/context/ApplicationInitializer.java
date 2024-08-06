package com.example.context;

import com.example.db.ArticleDatabase;
import com.example.db.ArticleMysqlDatabase;
import com.example.db.ReplyDatabase;
import com.example.db.ReplyMysqlDatabase;
import com.example.db.UserDatabase;
import com.example.db.UserMysqlDatabase;
import com.example.exception.BaseException;
import com.example.service.ArticleService;
import com.example.service.ReplyService;
import com.example.service.UserService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		UserDatabase userDatabase = new UserMysqlDatabase();
		ArticleDatabase articleDatabase = new ArticleMysqlDatabase();
		ReplyDatabase replyDatabase = new ReplyMysqlDatabase();
		sce.getServletContext().setAttribute("userDatabase", userDatabase);
		sce.getServletContext().setAttribute("replyDatabase", replyDatabase);
		sce.getServletContext().setAttribute("articleDatabase", articleDatabase);
		sce.getServletContext()
			.setAttribute("userService", new UserService(userDatabase, articleDatabase, replyDatabase));
		sce.getServletContext().setAttribute("articleService", new ArticleService(articleDatabase, replyDatabase));
		sce.getServletContext().setAttribute("replyService", new ReplyService(replyDatabase));

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw BaseException.serverException();
		}
	}
}
