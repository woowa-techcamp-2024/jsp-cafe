package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class QnaFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 질문 작성 Form으로 이동
        SendPageUtil.forward("/template/qna/form.jsp", getServletContext(), request, response);
    }
}
