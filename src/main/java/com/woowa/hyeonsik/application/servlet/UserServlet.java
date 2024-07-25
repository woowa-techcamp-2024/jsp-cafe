package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.service.UserService;
import com.woowa.hyeonsik.application.util.SendPageUtil;
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
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.findAll();
        request.setAttribute("users", users);
        logger.debug("회원 목록 조회, 유저 목록: {}", users);

        SendPageUtil.forward("/template/user/list.jsp", this.getServletContext(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        logger.debug("회원가입 요청도착! userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);

        User user = new User(userId, password, name, email);
        userService.signUp(user);

        SendPageUtil.redirect("/users", this.getServletContext(), response);
    }
}
