package lass9436.question.servlet;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.question.model.Question;
import lass9436.question.model.QuestionRepository;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;

@WebServlet("/questions/*")
public class QuestionDetailServlet extends HttpServlet {

	private QuestionRepository questionRepository;
	private UserRepository userRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionRepository = (QuestionRepository) config.getServletContext().getAttribute("questionRepository");
		userRepository = (UserRepository) config.getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo(); // 예: /123
		if (pathInfo != null && !pathInfo.isEmpty()) {
			long questionSeq = Long.parseLong(pathInfo.substring(1));
			String userId = (String) req.getSession().getAttribute("userId");
			User user = userRepository.findByUserId(userId);
			Question question = questionRepository.findByQuestionSeq(questionSeq);
			if (user != null && question != null && user.getUserSeq() == question.getUserSeq()) {
				questionRepository.deleteByQuestionSeq(questionSeq);
				resp.sendRedirect("/");
				return;
			}
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
			return;
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Question ID");
	}
}
