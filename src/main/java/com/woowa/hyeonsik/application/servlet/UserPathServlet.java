package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.exception.AuthorizationException;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.service.UserService;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserPathServlet.class);
    private final UserService userService;

    public UserPathServlet(UserService userService) {
        this.userService = userService;
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
            authorizeUserAccess(request, userId);
            SendPageUtil.forward("/template/user/updateForm.jsp", this.getServletContext(), request, response);
        } else {
            // 회원정보 확인
            SendPageUtil.forward("/template/user/profile.jsp", this.getServletContext(), request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // URI에서 유저ID를 가져온다.
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/users/".length());
        String[] split = requestURI.split("/");
        String userId = split[0];
        logger.debug("특정 유저의 정보를 수정합니다. UserID: {}", userId);

        // 적절한 접근이지 세션을 통해 확인한다.
        authorizeUserAccess(request, userId);

        // 유저 정보 업데이트
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        logger.debug("수정 정보! userId: {}, password: {}, name: {}, email: {}", userId, password, name, email);

        User user = new User(userId, password, name, email);
        userService.updateUser(user);

        request.setAttribute("user", user);
        SendPageUtil.forward("/template/user/updateForm.jsp", this.getServletContext(), request, response);
    }

    private void authorizeUserAccess(final HttpServletRequest request, final String userId) {
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 다른 사용자의 정보를 수정하려는 경우 예외 발생
        if (!sessionUser.getUserId().equals(userId)) {
            throw new AuthorizationException("다른 사용자 정보에 접근할 수 없습니다.");
        }
    }
}
