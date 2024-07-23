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

@WebServlet(name = "SignupServlet", urlPatterns = {"/users"})
public class SignupServlet extends HttpServlet {

	private UserDatabase userDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDatabase = (UserDatabase)getServletContext().getAttribute("userDatabase");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		if (userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		User user = new User(userId, password, name, email);
		userDatabase.insert(user);
		response.sendRedirect("/user/list.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect("/user/list.jsp");
	}
}
