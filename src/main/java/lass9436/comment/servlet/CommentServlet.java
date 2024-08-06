package lass9436.comment.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.comment.model.Comment;
import lass9436.comment.model.CommentRepository;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

	private CommentRepository commentRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		commentRepository = (CommentRepository)config.getServletContext().getAttribute("commentRepository");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long questionSeq = Long.parseLong(req.getParameter("questionSeq"));
		long startCommentSeq = Long.parseLong(req.getParameter("startCommentSeq"));
		int count = 5; // 또는 req.getParameter("count")로 받아올 수 있습니다.

		List<Comment> comments = commentRepository.findRangeByQuestionSeq(questionSeq, startCommentSeq, count);

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(comments);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(jsonResponse);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String contents = req.getParameter("contents");
		long questionSeq = Long.parseLong(req.getParameter("questionSeq"));
		String writer = (String) req.getSession(false).getAttribute("userName");
		long userSeq = (long)req.getSession(false).getAttribute("userSeq");
		Comment comment = new Comment(userSeq, questionSeq, writer, contents);
		commentRepository.save(comment);

		// JSON 응답 생성
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(comment);

		// 응답 설정
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(jsonResponse);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		long seq = Long.parseLong(req.getParameter("seq"));
		Comment comment = commentRepository.findByCommentSeq(seq);

		if (comment == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (req.getSession(false) == null){
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		if (comment.getUserSeq() != (long)req.getSession(false).getAttribute("userSeq")){
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		commentRepository.deleteByCommentSeq(seq);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
