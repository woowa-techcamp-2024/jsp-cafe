package com.example.servlet;

import static com.example.dto.util.DtoCreationUtil.*;

import java.io.IOException;

import com.example.dto.LoginRequest;
import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/users/login")
public class LoginServlet extends HttpServlet {

	private UserService userService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userService = (UserService)config.getServletContext().getAttribute("userService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = userService.login(createDto(LoginRequest.class, req));
		HttpSession session = req.getSession();
		session.setAttribute("login", true);
		session.setAttribute("name", user.name());
		session.setAttribute("id", user.id());
		resp.sendRedirect("/");
	}
}
