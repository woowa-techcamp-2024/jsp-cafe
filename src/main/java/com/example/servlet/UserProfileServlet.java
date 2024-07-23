package com.example.servlet;

import java.io.IOException;
import java.util.Optional;

import com.example.db.UserMemoryDatabase;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users/profile/*")
public class UserProfileServlet extends HttpServlet {

	private UserMemoryDatabase userMemoryDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userMemoryDatabase = (UserMemoryDatabase)config.getServletContext().getAttribute("userDatabase");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] split = req.getPathInfo().substring(1).split("/");
		String userId = split[0];
		Optional<User> userOptional = userMemoryDatabase.findById(userId);
		if (userOptional.isEmpty()) {
			throw new RuntimeException("User not found");
		}

		User user = userOptional.get();
		req.setAttribute("user", user);
		req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
	}
}
