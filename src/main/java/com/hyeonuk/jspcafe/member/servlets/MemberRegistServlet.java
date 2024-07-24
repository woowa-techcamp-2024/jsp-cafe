package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.InvalidMemberRegistRequest;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MemberRegistServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(MemberRegistServlet.class);
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        memberDao = (MemberDao)config.getServletContext().getAttribute("memberDao");
        passwordEncoder = (PasswordEncoder) config.getServletContext().getAttribute("passwordEncoder");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        req.getRequestDispatcher("/templates/user/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String encryptedPassword = passwordEncoder.encode(password);

        Member member = new Member(userId, encryptedPassword, nickname, email);
        if(!member.validation()) throw new InvalidMemberRegistRequest("값을 확인해주세요");
        if(memberDao.findByMemberId(userId).isPresent()) throw new InvalidMemberRegistRequest("이미 존재하는 유저입니다.");

        memberDao.save(member);
        resp.sendRedirect(req.getContextPath()+"/members");
    }
}
