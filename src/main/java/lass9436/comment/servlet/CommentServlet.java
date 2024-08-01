package lass9436.comment.servlet;

import java.io.IOException;

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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String contents = req.getParameter("contents");
		long questionSeq = Long.parseLong(req.getParameter("questionSeq"));
		String writer = (String) req.getSession(false).getAttribute("userName");
		long userSeq = (long)req.getSession(false).getAttribute("userSeq");
		Comment comment = new Comment(userSeq, questionSeq, writer, contents);
		commentRepository.save(comment);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
