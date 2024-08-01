package lass9436.question.servlet;

import java.io.IOException;
import java.util.List;

import lass9436.comment.model.Comment;
import lass9436.comment.model.CommentRepository;
import org.json.JSONObject;

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
import lass9436.utils.JsonUtil;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {

	private UserRepository userRepository;
	private QuestionRepository questionRepository;
	private CommentRepository commentRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionRepository = (QuestionRepository)config.getServletContext().getAttribute("questionRepository");
		userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
		commentRepository = (CommentRepository)config.getServletContext().getAttribute("commentRepository");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 폼 데이터 추출
		String writer = req.getParameter("writer");
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");

		String userId = (String)req.getSession().getAttribute("userId");
		User user = userRepository.findByUserId(userId);

		if (user != null && user.getName().equals(writer)) {
			questionRepository.save(new Question(user.getUserSeq(), writer, title, contents));
			resp.sendRedirect("/");
			return;
		}

		resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject json = JsonUtil.parseJson(req);
		long seq = json.getLong("seq");
		String writer = json.getString("writer");
		String title = json.getString("title");
		String contents = json.getString("contents");

		String userId = (String)req.getSession().getAttribute("userId");
		User user = userRepository.findByUserId(userId);

		if (user != null && user.getName().equals(writer)) {
			questionRepository.save(new Question(seq, user.getUserSeq(), writer, title, contents));
			resp.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject json = JsonUtil.parseJson(req);
		long seq = json.getLong("seq");
		Question question = questionRepository.findByQuestionSeq(seq);
		long userSeq = (long)req.getSession().getAttribute("userSeq");
		if (question.getUserSeq() != userSeq) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
		}
		// 댓글 중에 자기가 쓴 댓글이 아닌 게 있다면 글을 삭제할 수 없음
		List<Comment> comments = commentRepository.findByQuestionSeq(seq);
		for (Comment comment : comments) {
			if (comment.getUserSeq() != userSeq) {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized.");
				return;
			}
		}
		questionRepository.deleteByQuestionSeq(seq);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
