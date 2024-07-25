package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberControlServlet extends HttpServlet {
    private MemberDao memberDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        memberDao = (MemberDao)config.getServletContext().getAttribute("memberDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo.trim())){
           throw new HttpBadRequestException("잘못된 요청입니다.");
        }

        String memberId = pathInfo.substring(1);

        Member member = memberDao.findByMemberId(memberId)
                .orElseThrow(() -> new HttpNotFoundException("해당 유저를 찾을 수 없습니다."));
        req.setAttribute("member",member);
        req.getRequestDispatcher("/templates/user/profile.jsp").forward(req,resp);
    }
}
