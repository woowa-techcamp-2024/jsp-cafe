package com.woowa.servlet;

import com.woowa.database.user.UserDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.UserHandler;
import com.woowa.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class UserProfileServlet extends HttpServlet {

    private final UserHandler userHandler;
    private final UserDatabase userDatabase;

    public UserProfileServlet(UserHandler userHandler, UserDatabase userDatabase) {
        this.userHandler = userHandler;
        this.userDatabase = userDatabase;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        if (requestURI.endsWith("/edit")) {
            String userId = requestURI.replace("/users/", "").replace("/edit", "");
            ResponseEntity response = userHandler.updateUserForm(userId);
            req.setAttribute("user", response.getModel().get("user"));
            req.getRequestDispatcher("/user/update.jsp").forward(req, resp);
        } else {
            String userId = getUserId(req);
            User user = userDatabase.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
            req.setAttribute("user", user);
            req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
        }
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
