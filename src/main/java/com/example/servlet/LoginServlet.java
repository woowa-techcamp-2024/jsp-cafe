package com.example.servlet;

import java.io.IOException;
import java.util.Optional;

import com.example.db.UserDatabase;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/users/login")
public class LoginServlet extends HttpServlet {

	private UserDatabase userDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDatabase = (UserDatabase)config.getServletContext().getAttribute("userDatabase");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String password = req.getParameter("password");

		Optional<User> userOptional = userDatabase.findById(id);
		if (userOptional.isEmpty()) {
			req.getRequestDispatcher("/user/login_failed.jsp").forward(req, resp);
			return;
		}

		User user = userOptional.get();
		if (!user.password().equals(password)) {
			req.getRequestDispatcher("/user/login_failed.jsp").forward(req, resp);
			return;
		}

		HttpSession session = req.getSession();
		session.setAttribute("login", true);
		session.setAttribute("name", user.name());
		session.setAttribute("id", user.id());
		resp.sendRedirect("/");
	}
}
