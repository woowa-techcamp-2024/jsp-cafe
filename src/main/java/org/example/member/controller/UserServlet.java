package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.service.UserQueryService;
import org.example.member.service.UserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private UserRegisterService userRegisterService;
    private UserQueryService userQueryService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listUsers(req, resp);
            } else {
                showProfile(req, resp);
            }
        } catch (SQLException e) {
            logger.error("Error occurred while processing GET request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        List<UserResponseDto> users = userQueryService.findAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/jsp/user/list.jsp").forward(req, resp);
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String userId = req.getPathInfo().substring(1);
        logger.info("userId = {}", userId);
        UserResponseDto userResponseDto = userQueryService.findUserByUserId(userId);
        req.setAttribute("user", userResponseDto);
        req.getRequestDispatcher("/jsp/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userId = req.getParameter("userId");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String email = req.getParameter("email");

            User user = User.createUser(userId, password, name, email);
            userRegisterService.register(user);
            resp.sendRedirect("/users");
        } catch (Exception e) {
            logger.error("Error occurred while processing POST request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // TODO: 에러페이지 반환 로직 추가 예정, 예외를 분할해 상세한 정보 제공 처리 예정
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRegisterService = new UserRegisterService();
        this.userQueryService = new UserQueryService();
    }
}