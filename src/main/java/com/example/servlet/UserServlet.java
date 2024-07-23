package com.example.servlet;

import java.io.IOException;

import com.example.db.UserDatabase;
import com.example.db.UserMemoryDatabase;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

	private UserDatabase userDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDatabase = (UserDatabase)getServletContext().getAttribute("userDatabase");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("userList", userDatabase.findAll());
		req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		if (userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		User user = new User(userId, password, name, email);
		userDatabase.insert(user);
		resp.sendRedirect("/users");
	}
}
