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
		req.setAttribute("id", req.getPathInfo().substring(1));
		req.getRequestDispatcher("/user/updateForm.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		if (session.getAttribute("login") == null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		String userId = req.getPathInfo().substring(1);

		Optional<User> userOptional = userDatabase.findById(userId);
		if (userOptional.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		User user = userOptional.get();
		if (!session.getAttribute("id").equals(user.id())) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String name = req.getParameter("name");

		if (!user.password().equals(password)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		userDatabase.update(userId, new User(null, null, name, email));
		resp.sendRedirect("/users");
	}
}
