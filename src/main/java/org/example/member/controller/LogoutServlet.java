package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.member.repository.UserRepository;
import org.example.member.service.UserService;
import org.example.util.DataUtil;
import org.example.util.session.InMemorySessionManager;
import org.example.util.session.SessionManager;

@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {

    private UserService userService;
    private SessionManager sessionManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        req.getSession().invalidate();
        sessionManager.invalidateSession(sessionId);
        resp.sendRedirect("/");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService(new UserRepository(new DataUtil()));
        sessionManager = InMemorySessionManager.getInstance();
    }
}