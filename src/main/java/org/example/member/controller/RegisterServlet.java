package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.member.model.dao.User;
import org.example.member.service.UserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/users")
public class RegisterServlet extends HttpServlet {

    private UserRegisterService userRegisterService;
    private static final String USER_LIST_PAGE = "/list";
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getRequestDispatcher(USER_LIST_PAGE).forward(req, resp);
        } catch (ServletException | IOException e) {
            logger.error("Error occurred while processing request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
            resp.sendRedirect(USER_LIST_PAGE);
        } catch (Exception e) {
            // TODO: 에러페이지 반환 로직 추가 예정, 예외를 분할해 상세한 정보 제공 처리 예정
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRegisterService = new UserRegisterService();
    }
}
