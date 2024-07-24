package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListServlet extends HttpServlet {
    private MemberDao memberDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        memberDao = (MemberDao)config.getServletContext().getAttribute("memberDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Member> members = memberDao.findAll();
        req.setAttribute("members",members);
        req.getRequestDispatcher("/user/list.jsp").forward(req,resp);
    }
}
