package lass9436.question.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.question.model.Question;
import lass9436.question.model.QuestionRepository;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {

	private UserRepository userRepository;
	private QuestionRepository questionRepository;

	@Override
	public void init() throws ServletException {
		questionRepository = (QuestionRepository) getServletContext().getAttribute("questionRepository");
		userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 폼 데이터 추출
		String writer = req.getParameter("writer");
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");

		User user = userRepository.findByUserId(writer);

		// Question 객체 생성
		questionRepository.save(new Question(user.getUserSeq(), writer, title, contents));
		resp.sendRedirect("/");
	}
}
