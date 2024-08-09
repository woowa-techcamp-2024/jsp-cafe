package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesquad.cafe.db.dao.CommentDao;
import com.codesquad.cafe.db.dao.PostRepository;
import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.model.aggregate.CommentWithUser;
import com.codesquad.cafe.model.aggregate.PostDetail;
import com.codesquad.cafe.model.aggregate.PostWithAuthor;
import com.codesquad.cafe.model.dto.ErrorResponse;
import com.codesquad.cafe.model.dto.PostCreateRequest;
import com.codesquad.cafe.model.dto.PostUpdateRequest;
import com.codesquad.cafe.model.dto.RedirectResponse;
import com.codesquad.cafe.service.PostService;
import com.codesquad.cafe.util.JsonModelMapper;
import com.codesquad.cafe.util.RequestParamModelMapper;
import com.codesquad.cafe.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private PostRepository postRepository;

	private CommentDao commentDao;

	private PostService postService;

	@Override
	public void init() throws ServletException {
		super.init();
		this.postRepository = (PostRepository)getServletContext().getAttribute("postRepository");
		this.postService = (PostService)getServletContext().getAttribute("postService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			resp.sendRedirect("/");
			return;
		}
		Long id = parsePathVariable(pathInfo);
		if (pathInfo.matches("/\\d+/edit")) {
			showEditForm(req, resp, id); // {id}
		} else if (pathInfo.matches("/\\d+")) {
			viewPost(req, resp, id); // {id}
		} else {
			throw new ResourceNotFoundException();
		}
	}

	private void viewPost(HttpServletRequest req, HttpServletResponse resp, Long id)
		throws ServletException, IOException {
		Post post = postRepository.findById(id)
			.orElseThrow(ResourceNotFoundException::new);
		postRepository.addView(post);

		PostDetail postDetail = postService.getPostDetailWithPagedComments(id);

		req.setAttribute("post", postDetail);
		req.getRequestDispatcher("/WEB-INF/views/post_detail.jsp").forward(req, resp);
	}

	private void showEditForm(HttpServletRequest req, HttpServletResponse resp, Long id)
		throws ServletException, IOException {
		PostWithAuthor postWithDetail = postRepository.findPostWithAuthorById(id)
			.orElseThrow(ResourceNotFoundException::new);

		UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);
		if (!userPrincipal.getId().equals(postWithDetail.getAuthorId())) {
			throw new AuthorizationException();
		}
		req.setAttribute("post", postWithDetail);
		req.getRequestDispatcher("/WEB-INF/views/post_edit_form.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PostCreateRequest requestDto = RequestParamModelMapper.map(req.getParameterMap(),
			PostCreateRequest.class);

		requestDto.validate();

		UserPrincipal userPrincipal = getUserPrincipal(req);
		if (!requestDto.getAuthorId().equals(userPrincipal.getId())) {
			throw new AuthorizationException();
		}

		postRepository.save(requestDto.toPost());

		resp.sendRedirect("/");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);

		Long postId = parsePathVariable(req.getPathInfo());
		PostUpdateRequest requestDto = JsonModelMapper.map(req, PostUpdateRequest.class);
		requestDto.validate();

		// find original post
		Post post = postRepository.findById(postId)
			.orElseThrow(ResourceNotFoundException::new);

		// validate request
		if (!post.getAuthorId().equals(requestDto.getAuthorId())) {
			resp.setStatus(400);
			JsonModelMapper.json(resp, new ErrorResponse("작성자가 일치하지 않습니다."));
		}

		// authorization
		if (userPrincipal == null || !userPrincipal.getId().equals(post.getAuthorId())) {
			throw new AuthorizationException();
		}

		// update post
		post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getFileName());
		postRepository.save(post);
		JsonModelMapper.json(resp, new RedirectResponse("/"));
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);

		PostDetail post = postService.getPostDetail(parsePathVariable(req.getPathInfo()));

		if (userPrincipal == null || !userPrincipal.getId().equals(post.getAuthorId())) {
			throw new AuthorizationException();
		}

		if (!canDelete(post)) {
			resp.setStatus(400);
			JsonModelMapper.json(resp, new ErrorResponse("게시글을 삭제할 수 없습니다."));
			return;
		}

		postRepository.softDeletePostWithComments(post.getPostId());
		JsonModelMapper.json(resp, new RedirectResponse("/"));
	}

	private boolean canDelete(PostDetail post) {
		if (post.getComments().isEmpty()) {
			return true;
		}
		for (CommentWithUser commentWithUser : post.getComments()) {
			if (!commentWithUser.getUserId().equals(post.getAuthorId())) {
				return false;
			}
		}
		return true;
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
