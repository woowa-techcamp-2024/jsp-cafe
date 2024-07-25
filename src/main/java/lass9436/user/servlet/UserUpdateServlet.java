package lass9436.user.servlet;

import java.io.IOException;

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
	public void init() throws ServletException {
		// 서블릿 초기화 시 컨텍스트에서 UserRepository 인스턴스를 가져옴
		userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		long userSeq = Long.parseLong(pathInfo.split("/")[1]);
		User user = userRepository.findByUserSeq(userSeq);
		if (user == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
			return;
		}

		req.setAttribute("user", user);
		req.getRequestDispatcher("/WEB-INF/user/update.jsp").forward(req, resp);
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
		if (user == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
			return;
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
