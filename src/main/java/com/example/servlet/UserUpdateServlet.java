package com.example.servlet;

import java.io.IOException;

import com.example.dto.UserUpdateRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.service.UserService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/users/edit/*")
public class UserUpdateServlet extends HttpServlet {

	private UserService userService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userService = (UserService)config.getServletContext().getAttribute("userService");
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
		String id = req.getPathInfo().substring(1);
		UserUpdateRequest dto = DtoCreationUtil.createDto(UserUpdateRequest.class, req);
		dto.validate();
		userService.updateUser(id, dto);
		resp.sendRedirect("/users");
	}
}
