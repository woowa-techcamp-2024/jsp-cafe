package com.example.servlet;

import java.io.IOException;

import com.example.db.UserDatabase;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users/edit/*")
public class UserUpdateServlet extends HttpServlet {

	private UserDatabase userDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDatabase = (UserDatabase)config.getServletContext().getAttribute("userDatabase");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("userId", req.getPathInfo().substring(1));
		req.getRequestDispatcher("/user/updateForm.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String userId = req.getPathInfo().substring(1);

		if (userDatabase.findById(userId).isEmpty()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String name = req.getParameter("name");

		userDatabase.update(userId, new User(userId, password, name, email));
		resp.sendRedirect("/users");
	}
}
