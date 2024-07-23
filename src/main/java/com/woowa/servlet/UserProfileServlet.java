package com.woowa.servlet;

import com.woowa.database.UserDatabase;
import com.woowa.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class UserProfileServlet extends HttpServlet {

    private final UserDatabase userDatabase;

    public UserProfileServlet(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String userId = requestURI.replace("/users/", "");
        User user = userDatabase.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/classes/static/user/profile.jsp").forward(req, resp);
    }
}
