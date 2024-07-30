package com.example.servlet;

import java.io.IOException;

import com.example.db.UserDatabase;
import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserProfileServlet", urlPatterns = "/users/profile/*")
public class UserProfileServlet extends HttpServlet {

	private UserService userService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userService = (UserService)config.getServletContext().getAttribute("userService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = userService.getUser(req.getPathInfo().substring(1).split("/")[0]);
		req.setAttribute("user", user);
		req.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(req, resp);
	}
}
