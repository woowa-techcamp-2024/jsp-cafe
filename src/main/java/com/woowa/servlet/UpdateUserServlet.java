package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.UserHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateUserServlet extends HttpServlet {

    private final UserHandler userHandler;

    public UpdateUserServlet(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = getUserId(req);
        String nickname = req.getParameter("nickname");
        ResponseEntity response = userHandler.updateUser(userId, nickname);
        resp.sendRedirect(response.getLocation());
    }

    private static String getUserId(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.replace("/users/", "").replace("/edit", "");
    }
}
