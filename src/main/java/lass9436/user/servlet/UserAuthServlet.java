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

@WebServlet("/users/auth")
public class UserAuthServlet extends HttpServlet {

	private UserRepository userRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			// 요청에서 id 와 password 가져오기
			String userId = req.getParameter("userId");
			String password = req.getParameter("password");

			// 레포지토리에서 user 가져오기
			User user = userRepository.findByUserId(userId);
			if (user == null) {
				throw new IllegalArgumentException("User not found");
			}

			// 비밀번호 확인
			if (!password.equals(user.getPassword()))
				throw new IllegalArgumentException("Wrong password");

			// 세션에 저장
			req.getSession().setAttribute("userId", userId);
			req.getSession().setAttribute("userSeq", user.getUserSeq());
			req.getSession().setAttribute("userName", user.getName());

			// 메인 페이지로 리다이렉트
			resp.sendRedirect("/");
		} catch (RuntimeException e) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 세션 무효화
		req.getSession().invalidate();
		// 200 OK 응답
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
