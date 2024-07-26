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

@WebServlet("/users/*")
public class UserPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserPathServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/users/".length());
        String[] split = requestURI.split("/");
        String userId = split[0];
        logger.debug("특정 유저의 프로필을 조회합니다. UserID: {}", userId);

        User user = userService.findByUserId(userId);
        request.setAttribute("user", user);

        if (requestURI.endsWith("/form")) {
            // 회원정보 수정
            SendPageUtil.forward("/template/user/updateForm.jsp", this.getServletContext(), request, response);
        } else {
            // 회원정보 확인
            SendPageUtil.forward("/template/user/profile.jsp", this.getServletContext(), request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/users/".length());
        String[] split = requestURI.split("/");
        String userId = split[0];
        logger.debug("특정 유저의 정보를 수정합니다. UserID: {}", userId);

        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        logger.debug("수정 정보! userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);

        User user = new User(userId, password, name, email);
        userService.updateUser(user);

        request.setAttribute("user", user);
        SendPageUtil.forward("/template/user/updateForm.jsp", this.getServletContext(), request, response);
    }
}
