package com.example.context;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.example.db.ArticleMysqlDatabase;
import com.example.db.UserMysqlDatabase;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("userDatabase", new UserMysqlDatabase());
		sce.getServletContext().setAttribute("articleDatabase", new ArticleMysqlDatabase());
		String url = "jdbc:mysql://localhost:3306/codesquad";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try (
			Connection connection = DriverManager.getConnection(url, "root", "root");
			Statement statement = connection.createStatement();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
			if (inputStream == null) {
				throw new RuntimeException("not found schema.sql");
			}
			Scanner scanner = new Scanner(inputStream);
			scanner.useDelimiter(";");
			while (scanner.hasNext()) {
				String sql = scanner.next().trim();
				if (!sql.isEmpty()) {
					statement.execute(sql);
				}
			}
			scanner.close();
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
