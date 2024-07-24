package com.woowa.servlet;

import com.woowa.database.UserDatabase;
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
        String userId = getUserId(req);
        User user = userDatabase.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/classes/static/user/profile.jsp").forward(req, resp);
    }

    private String getUserId(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.replace("/users/", "");
    }
}
