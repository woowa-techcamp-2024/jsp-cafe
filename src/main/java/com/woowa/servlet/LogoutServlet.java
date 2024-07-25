package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.LoginHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    private final LoginHandler loginHandler;

    public LogoutServlet(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseEntity response = loginHandler.logout(req);
        resp.sendRedirect(response.getLocation());
    }
}
