package com.wootecam.jspcafe.servlet;

import com.wootecam.jspcafe.model.User;
import com.wootecam.jspcafe.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignupServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SignupServlet.class);

    private final UserRepository userRepository;

    public SignupServlet(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/user/form.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        User user = new User(userId, password, name, email);

        userRepository.save(user);
        log.info("signUpUser = {}", user);

        resp.sendRedirect("/users");
    }
}
