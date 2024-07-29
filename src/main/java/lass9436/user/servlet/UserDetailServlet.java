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

@WebServlet("/users/*")
public class UserDetailServlet extends HttpServlet {

	private UserRepository userRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// URL 패턴에서 경로 변수를 추출
		String pathInfo = req.getPathInfo(); // 예: /123
		if (pathInfo != null && !pathInfo.isEmpty()) {
			long userId = Long.parseLong(pathInfo.substring(1)); // 첫 번째 슬래시를 제거
			User user = userRepository.findByUserSeq(userId);
			if (user != null) {
				req.setAttribute("user", user);
				req.getRequestDispatcher("/WEB-INF/user/detail.jsp").forward(req, resp);
			} else {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
			}
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID");
		}
	}
}