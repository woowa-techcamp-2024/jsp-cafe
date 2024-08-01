package com.example.servlet;

import static com.example.dto.util.DtoCreationUtil.*;

import java.io.IOException;

import com.example.db.UserDatabase;
import com.example.dto.SignupRequest;
import com.example.service.UserService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {

	private UserDatabase userDatabase;
	private UserService userService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userDatabase = (UserDatabase)getServletContext().getAttribute("userDatabase");
		userService = (UserService)getServletContext().getAttribute("userService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("userList", userDatabase.findAll());
		req.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		SignupRequest dto = createDto(SignupRequest.class, req);
		dto.validate();
		userService.signup(dto);
		resp.sendRedirect("/");
	}
}
