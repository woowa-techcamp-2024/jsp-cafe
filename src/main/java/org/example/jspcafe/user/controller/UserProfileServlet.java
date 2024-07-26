package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.user.response.UserProfileResponse;
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;

@WebServlet(name = "userServlet", value = "/users/*")
public class UserProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = ApplicationContext.getContainer().resolve(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "닉네임이 올바르지 않습니다.");
            return;
        }

        String nickname = pathInfo.substring(1); // "/{nickname}"에서 닉네임을 추출
        UserProfileResponse userProfile = userService.getProfile(nickname);

        request.setAttribute("userProfile", userProfile);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}
