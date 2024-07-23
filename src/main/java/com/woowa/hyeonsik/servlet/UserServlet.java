package com.woowa.hyeonsik.servlet;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.domain.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private UserDao userDao;

    @Override

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<User> all = userDao.findAll();
        logger.debug("회원 목록 조회, 유저 목록: {}", all);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        logger.debug("회원가입 요청도착! userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);

        User user = new User(userId, password, name, email);
        userDao.add(user);

        response.sendRedirect("/users");
    }
}
