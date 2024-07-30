package lass9436.user.servlet;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;

@WebServlet("/users/update/*")
public class UserUpdateServlet extends HttpServlet {

	private UserRepository userRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] pathParts = req.getPathInfo().split("/");
		long userSeq = Long.parseLong(pathParts[1]);
		String password = req.getParameter("password");
		String newPassword = req.getParameter("new-password");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		User user = userRepository.findByUserSeq(userSeq);
		String userId = (String) req.getSession().getAttribute("userId");
		if (user == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
			return;
		}
		if (!user.getUserId().equals(userId)){
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
		}
		if (!user.getPassword().equals(password)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong password.");
			return;
		}
		user.setPassword(newPassword);
		user.setName(name);
		user.setEmail(email);
		userRepository.save(user);
		resp.sendRedirect("/users/" + userSeq);
	}
}
