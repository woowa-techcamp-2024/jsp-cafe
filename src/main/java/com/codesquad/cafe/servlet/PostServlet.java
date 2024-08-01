package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.PostDetailsDto;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.ErrorResponse;
import com.codesquad.cafe.model.PostCreateRequest;
import com.codesquad.cafe.model.PostUpdateRequest;
import com.codesquad.cafe.model.RedirectResponse;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.JsonModelMapper;
import com.codesquad.cafe.util.RequestParamModelMapper;
import com.codesquad.cafe.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (PostRepository) getServletContext().getAttribute("postRepository");
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
                .orElseThrow(() -> new ResourceNotFoundException());
        postRepository.addView(post);

        PostDetailsDto postWithDetail = postRepository.findPostWithAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        req.setAttribute("post", postWithDetail);
        req.getRequestDispatcher("/WEB-INF/views/post_detail.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, Long id)
            throws ServletException, IOException {
        PostDetailsDto postWithDetail = postRepository.findPostWithAuthorById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

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
                .orElseThrow(() -> new ResourceNotFoundException());

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
        PostDetailsDto postDetailsDto = postRepository.findPostWithAuthorById(postId)
                .orElseThrow(() -> new ResourceNotFoundException());

        req.setAttribute("post", postDetailsDto);
        JsonModelMapper.json(resp, new RedirectResponse("/"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);

        Long id = parsePathVariable(req.getPathInfo());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        // authorization
        if (userPrincipal == null || !userPrincipal.getId().equals(post.getAuthorId())) {
            throw new AuthorizationException();
        }

        post.delete();
        postRepository.save(post);
        JsonModelMapper.json(resp, new RedirectResponse("/"));
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
