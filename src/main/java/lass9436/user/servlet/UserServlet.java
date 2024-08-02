package lass9436.user.servlet;

import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;
import lass9436.utils.JsonUtil;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

	private UserRepository userRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 요청 파라미터 읽기
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		String name = req.getParameter("name");
		String email = req.getParameter("email");

		// User 객체 생성 및 데이터 설정
		User user = new User(userId, password, name, email);
		User save = userRepository.save(user);

		// 유저목록으로 리다이렉트
		resp.sendRedirect("/userPage?action=list");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			// JSON 파싱
			JSONObject json = JsonUtil.parseJson(req);

			// 요청 값 초기화
			String userId = json.getString("userId");
			String password = json.getString("password");
			String newPassword = json.getString("new-password");
			String name = json.getString("name");
			String email = json.getString("email");

			// 수정할 유저 가져오기
			User user = userRepository.findByUserId(userId);

			// 현재 로그인한 유저 가져오기
			String sessionUserId = (String)req.getSession().getAttribute("userId");

			// 레포지토리에 유저가 없으면 예외
			if (user == null) {
				throw new IllegalArgumentException("User not found");
			}

			// 로그인된 사용자와 수정하려는 유저가 다를 경우 예외
			if (!user.getUserId().equals(sessionUserId)) {
				throw new IllegalArgumentException("User not logged in");
			}

			// 입력한 비밀번호와 현재 비밀번호가 다를 경우 예외
			if (!user.getPassword().equals(password)) {
				throw new IllegalArgumentException("Wrong password");
			}

			// 유저 정보 수정 및 저장
			user.setPassword(newPassword);
			user.setName(name);
			user.setEmail(email);
			userRepository.save(user);

			// 200 OK 응답
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (RuntimeException e) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}
}

