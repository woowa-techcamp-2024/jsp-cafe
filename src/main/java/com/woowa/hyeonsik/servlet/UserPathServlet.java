package com.woowa.hyeonsik.servlet;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/users/*")
public class UserPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserPathServlet.class);
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        String userId = split[split.length - 1];
        logger.debug("특정 유저의 프로필을 조회합니다. UserID: {}", userId);

        User user = userDao.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
        request.setAttribute("user", user);

        ServletContext app = this.getServletContext();
        RequestDispatcher dispatcher = app.getRequestDispatcher("/template/user/profile.jsp");
        dispatcher.forward(request, response);
    }
}
