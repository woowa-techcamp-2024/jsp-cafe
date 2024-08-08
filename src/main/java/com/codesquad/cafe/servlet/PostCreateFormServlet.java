package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.dao.PostRepository;
import com.codesquad.cafe.db.dao.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostCreateFormServlet extends HttpServlet {

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

}
