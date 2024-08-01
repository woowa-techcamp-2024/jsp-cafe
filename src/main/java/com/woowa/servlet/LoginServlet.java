package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.LoginHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final LoginHandler loginHandler;

    public LoginServlet(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        ResponseEntity response = loginHandler.login(email, password, req);
        resp.sendRedirect(response.getLocation());
    }
}
