package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.*;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesquad.cafe.db.dao.CommentDao;
import com.codesquad.cafe.db.domain.Comment;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.model.aggregate.CommentWithUser;
import com.codesquad.cafe.model.dto.CommentCreateRequest;
import com.codesquad.cafe.model.dto.CommentsResponse;
import com.codesquad.cafe.util.JsonModelMapper;
import com.codesquad.cafe.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommentServlet extends HttpServlet {

	private static final int DEFAULT_COMMENT_SIZE = 5;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private CommentDao commentDao;

	@Override
	public void init() throws ServletException {
		super.init();
		this.commentDao = (CommentDao)getServletContext().getAttribute("commentDao");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long postId = Long.parseLong(req.getParameter("postId"));
		Long lastCommentId = Long.parseLong(req.getParameter("lastCommentId"));
		List<CommentWithUser> noOffsetCommentsByPostId = commentDao.findNoOffsetCommentsByPostId(postId, lastCommentId,
			DEFAULT_COMMENT_SIZE);
		JsonModelMapper.json(resp, new CommentsResponse(noOffsetCommentsByPostId));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserPrincipal userPrincipal = getUserPrincipal(req);

		CommentCreateRequest requestDto = JsonModelMapper.map(req, CommentCreateRequest.class);
		requestDto.validate();

		commentDao.save(requestDto.toComment(userPrincipal.getId()));
		List<CommentWithUser> comments = commentDao
			.findNoOffsetCommentsByPostId(requestDto.getPostId(), null, DEFAULT_COMMENT_SIZE);
		JsonModelMapper.json(resp, new CommentsResponse(comments));
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);

		Long id = parsePathVariable(req.getPathInfo());

		Comment comment = commentDao.findById(id)
			.orElseThrow(ResourceNotFoundException::new);

		if (userPrincipal == null || !userPrincipal.getId().equals(comment.getUserId())) {
			throw new AuthorizationException();
		}

		comment.delete();
		commentDao.save(comment);
	}

	private Long parsePathVariable(String pathInfo) {
		try {
			String[] paths = pathInfo.substring(1).split("/");
			return Long.parseLong(paths[0]);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException();
		}
	}

}
