package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.model.PostCreateRequest;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostCreateServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private PostRepository postRepository;

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (PostRepository) getServletContext().getAttribute("postRepository");
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/post_create_form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PostCreateRequest requestDto;

        requestDto = RequestParamModelMapper.map(req.getParameterMap(),
                PostCreateRequest.class);

        requestDto.validate();

        UserPrincipal userPrincipal = getUserPrincipal(req);
        if (userPrincipal == null || !requestDto.getAuthorId().equals(userPrincipal.getId())) {
            resp.sendError(401);
            return;
        }

        if (userRepository.findById(requestDto.getAuthorId()).isEmpty()) {
            resp.sendError(400);
            return;
        }

        postRepository.save(requestDto.toPost());

        resp.sendRedirect("/");
    }

}
