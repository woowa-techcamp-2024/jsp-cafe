package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.debug("로그아웃을 수행하려고 했지만 세션 값이 존재하지 않습니다.");
        } else {
            logger.debug("로그아웃을 수행합니다. 세션ID: {}", session.getId());
            session.invalidate();
            SendPageUtil.redirect("", getServletContext(), response);
        }
    }
}
