package com.woowa.hyeonsik.servlet;

import com.woowa.hyeonsik.domain.User;
import com.woowa.hyeonsik.service.UserService;
import com.woowa.hyeonsik.util.SendPageUtil;
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
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        String userId = split[split.length - 1];
        logger.debug("특정 유저의 프로필을 조회합니다. UserID: {}", userId);

        User user = userService.findByUserId(userId);
        request.setAttribute("user", user);

        SendPageUtil.forward("/template/user/profile.jsp", this.getServletContext(), request, response);
    }
}
