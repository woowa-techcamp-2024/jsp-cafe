package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.service.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/users/*")
public class UserProfileServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServlet.class);
    private UserQueryService userQueryService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            String userId = pathInfo.substring(1);
            logger.info("userId = {}", userId);
            UserResponseDto userResponseDto = userQueryService.findUserByUserId(userId);
            req.setAttribute("user", userResponseDto);
            req.getRequestDispatcher("/jsp/user/profile.jsp").forward(req, resp);
        } catch (SQLException e) {

        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        userQueryService = new UserQueryService();
    }
}
