package org.example.jspcafe.user.controller;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.MemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsersServlet", value = "/users")
public class UsersServlet extends HttpServlet {
    private UserService userService;
    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(UserServlet.class);
    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.userRepository = (UserRepository) context.getAttribute("UserRepository");
        this.userService = (UserService) context.getAttribute("UserService");
        logger.info("UserServlet init");
    }

    public UsersServlet() {
        this.userRepository = new MemoryUserRepository();
        this.userService = new UserService(userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> all = userRepository.getAll();
        req.setAttribute("users", all);
        req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String userId = req.getParameter("userId");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        User user = User.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
        userService.register(user);
        resp.sendRedirect("users");
    }
}
