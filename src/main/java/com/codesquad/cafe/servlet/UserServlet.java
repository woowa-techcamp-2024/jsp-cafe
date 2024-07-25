package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.InMemoryUserRepository;
import com.codesquad.cafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private InMemoryUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (InMemoryUserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getPathInfo().substring(1));
        log.warn("pathInfo: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            req.getRequestDispatcher("/static/error/404.html").forward(req, resp);
            return;
        }
        req.setAttribute("user", user.get());
        req.getRequestDispatcher("/WEB-INF/views/user_profile.jsp").forward(req, resp);
    }
}
