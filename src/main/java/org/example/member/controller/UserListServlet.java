package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.service.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/list")
public class UserListServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserListServlet.class);
    private UserQueryService userQueryService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<UserResponseDto> users = userQueryService.findAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/jsp/user/list.jsp").forward(req, resp);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        userQueryService = new UserQueryService();
    }
}
