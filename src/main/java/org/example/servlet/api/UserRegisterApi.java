package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import org.example.config.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/api/users")
public class UserRegisterApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserRegisterApi.class);
    private UserDataHandler userDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDataHandler = (UserDataHandler) config.getServletContext().getAttribute(DataHandler.USER.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        User user = new User(email, nickname, password, LocalDateTime.now());
        User savedUser = userDataHandler.save(user);
        log.debug("[UserRegisterApi]" + savedUser.toString());
        response.sendRedirect("/users");
    }
}
