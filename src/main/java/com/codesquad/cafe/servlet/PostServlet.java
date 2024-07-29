package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.PostDetailsDto;
import com.codesquad.cafe.exception.DataIntegrationException;
import com.codesquad.cafe.model.PostUpdateRequest;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PostServlet.class);

    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (PostRepository) getServletContext().getAttribute("postRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            resp.sendError(400);
        }
        try {
            Long postId = parsePathVariable(req.getPathInfo());

            Optional<PostDetailsDto> postWithDetail = postRepository.findPostWithAuthorById(postId);
            if (postWithDetail.isEmpty()) {
                resp.sendError(404);
                return;
            }
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                resp.sendError(404);
                return;
            }
            postRepository.addView(post.get());
            req.setAttribute("post", postWithDetail.get());
            req.getRequestDispatcher("/WEB-INF/views/post_detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        } catch (NullPointerException | DataIntegrationException e) {
            resp.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if (method != null && method.toLowerCase(Locale.ROOT).equals("delete")) {
            doDelete(req, resp);
            return;
        }
        resp.sendError(405);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long postId = parsePathVariable(req.getPathInfo());
            PostUpdateRequest requestDto = RequestParamModelMapper.map(req.getParameterMap(), PostUpdateRequest.class);

            // find original post
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isEmpty()) {
                resp.sendError(404);
                return;
            }

            // validate request
            Post post = optionalPost.get();
            if (!post.getAuthorId().equals(requestDto.getAuthorId())) {
                resp.sendError(400);
                return;
            }

            // update post
            post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getFileName());
            Post updatedPost = postRepository.save(post);

            req.setAttribute("post", updatedPost);
            req.getRequestDispatcher("/WEB-INF/views/post_detail.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            resp.sendError(400);
        } catch (NullPointerException | DataIntegrationException e) {
            resp.sendError(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = parsePathVariable(req.getPathInfo());
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                resp.sendError(400);
            }
            Post originalPost = post.get();
            originalPost.delete();
            postRepository.save(originalPost);
            resp.sendRedirect("/");
        } catch (NumberFormatException e) {
            resp.sendError(404);
        }
    }

    private Long parsePathVariable(String pathInfo) {
        String[] paths = pathInfo.substring(1).split("/");
        return Long.parseLong(paths[0]);
    }

}
