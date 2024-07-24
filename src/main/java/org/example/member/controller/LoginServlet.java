package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import org.example.member.repository.UserRepository;
import org.example.member.service.UserService;
import org.example.util.DataUtil;
import org.example.util.session.InMemorySessionManager;
import org.example.util.session.SessionManager;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private SessionManager sessionManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        try {
            if (userService.validateUser(userId, password)) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", userId);

                resp.sendRedirect("/");
            } else {
                resp.sendRedirect("/user/login_failed.html");
            }
        } catch (SQLException e) {
            resp.sendRedirect("/user/login_failed.html");
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService(new UserRepository(new DataUtil()));
        sessionManager = InMemorySessionManager.getInstance();
    }
}