package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class MemberLoginServlet extends HttpServlet {
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        memberDao = (MemberDao)config.getServletContext().getAttribute("memberDao");
        passwordEncoder = (PasswordEncoder) config.getServletContext().getAttribute("passwordEncoder");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        req.getRequestDispatcher("/templates/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        Member member = memberDao.findByMemberId(userId)
                .orElseThrow(() -> new HttpNotFoundException("계정이 존재하지 않습니다."));

        if(!passwordEncoder.match(password,member.getPassword())) throw new HttpBadRequestException("비밀번호가 일치하지 않습니다.");

        HttpSession session = req.getSession(true);

        session.setAttribute("member",member);

        resp.sendRedirect("/");
    }
}
