package org.example.servlet.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/api/users/update/*")
public class UserUpdateApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserUpdateApi.class);
    private UserDataHandler userDataHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        userDataHandler = (UserDataHandler) getServletContext().getAttribute(DataHandler.USER.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        Long userId = Long.valueOf(request.getParameter("userId"));
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        User user = userDataHandler.findByUserId(userId);
        if(!user.getPassword().equals(password)){
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "기존 비밀번호와 맞지 않습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return;
        }
        User updateUser = new User(user.getUserId(), email, nickname, password, user.getCreatedDt());
        log.debug("[UserUpdateApi] user" + updateUser.toString());
        userDataHandler.save(updateUser);
        response.sendRedirect("/users/" +user.getUserId());
    }
}
