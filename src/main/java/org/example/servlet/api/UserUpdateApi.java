package org.example.servlet.api;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/api/users/update/*")
public class UserUpdateApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserUpdateApi.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        User user = new User(1L, email, nickname, password, LocalDateTime.now());
        log.debug("[UserUpdateApi] user" + user.toString());
        response.sendRedirect("/users/" +user.getUserId());
    }
}
