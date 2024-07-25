package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.model.UserJoinRequest;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserJoinServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserJoinServlet.class);

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user_join_form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserJoinRequest user = RequestParamModelMapper.map(req.getParameterMap(), UserJoinRequest.class);

        log.debug("user: {}", user);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            req.setAttribute("error", "중복된 이메일입니다.");
            req.getRequestDispatcher("/WEB-INF/views/user_join_form.jsp").forward(req, resp);
            return;
        }

        try {
            userRepository.save(user.toUser());
        } catch (IllegalArgumentException e) {
            resp.sendError(400);
            return;
        }

        resp.sendRedirect("/users");
    }
}