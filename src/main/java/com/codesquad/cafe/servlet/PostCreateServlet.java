package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.InMemoryPostRepository;
import com.codesquad.cafe.db.InMemoryUserRepository;
import com.codesquad.cafe.exception.ModelMappingException;
import com.codesquad.cafe.model.PostCreateRequest;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostCreateServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PostsServlet.class);

    private InMemoryPostRepository postRepository;

    private InMemoryUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (InMemoryPostRepository) getServletContext().getAttribute("postRepository");
        this.userRepository = (InMemoryUserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/post_create_form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PostCreateRequest requestDto = RequestParamModelMapper.map(req.getParameterMap(),
                    PostCreateRequest.class);

            if (userRepository.findById(requestDto.getAuthorId()).isEmpty()) {
                throw new IllegalArgumentException("user not found by id : " + requestDto.getAuthorId());
            }

            postRepository.save(requestDto.toPost());

            resp.sendRedirect("/");
        } catch (IllegalArgumentException | ModelMappingException e) {
            resp.sendError(400);
        }
    }

}
