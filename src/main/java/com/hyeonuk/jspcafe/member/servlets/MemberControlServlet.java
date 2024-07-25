package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.global.exception.InvalidMemberRegistRequest;
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

public class MemberControlServlet extends HttpServlet {
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    @Override
    public void init(ServletConfig config) throws ServletException {
        memberDao = (MemberDao)config.getServletContext().getAttribute("memberDao");
        passwordEncoder = (PasswordEncoder)config.getServletContext().getAttribute("passwordEncoder");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo.trim())){
           throw new HttpBadRequestException("잘못된 요청입니다.");
        }

        String[] pathParts = pathInfo.split("/");
        if(pathParts.length < 2){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        String memberId = pathParts[1];
        if(pathParts.length == 2){
            forwardToProfile(req, resp, memberId);
        }
        else if(pathParts.length == 3 && "form".equals(pathParts[2])){
            forwardToProfileUpdate(req, resp, memberId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo.trim())){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }

        HttpSession session = req.getSession();
        if(session==null || session.getAttribute("member")==null){
            resp.sendRedirect("/login");
            return;
        }


        String[] pathParts = pathInfo.split("/");
        if(pathParts.length < 2 || pathParts.length > 3){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        String memberId = pathParts[1];
        Member member = (Member) session.getAttribute("member");
        if(!member.getMemberId().equals(memberId)) {
            resp.sendRedirect("/login");
            return;
        }
        Member origin = memberDao.findByMemberId(memberId)
                .orElseThrow(() -> new HttpNotFoundException("해당 유저를 찾을 수 없습니다."));
        String passwordCheck = req.getParameter("passwordCheck");
        if(!passwordEncoder.match(passwordCheck,origin.getPassword())) throw new HttpBadRequestException("비밀번호가 일치하지 않습니다.");

        //update 로직
        String newPassword = req.getParameter("password");
        String newEmail = req.getParameter("email");
        String newNickname = req.getParameter("nickname");

        Member newMember = new Member(origin.getId(),origin.getMemberId(),passwordEncoder.encode(newPassword),newNickname,newEmail);
        if(!newMember.validation()) throw new InvalidMemberRegistRequest("값을 확인해주세요");

        memberDao.save(newMember);

        resp.sendRedirect("/members/"+newMember.getMemberId());
    }

    private void forwardToProfileUpdate(HttpServletRequest req, HttpServletResponse resp, String memberId) throws ServletException, IOException {
        Member member = memberDao.findByMemberId(memberId)
                .orElseThrow(() -> new HttpNotFoundException("해당 유저를 찾을 수 없습니다."));
        req.setAttribute("member",member);
        req.getRequestDispatcher("/templates/user/profile_update.jsp").forward(req, resp);
    }

    private void forwardToProfile(HttpServletRequest req, HttpServletResponse resp, String memberId) throws ServletException, IOException {
        Member member = memberDao.findByMemberId(memberId)
                .orElseThrow(() -> new HttpNotFoundException("해당 유저를 찾을 수 없습니다."));
        req.setAttribute("member",member);
        req.getRequestDispatcher("/templates/user/profile.jsp").forward(req, resp);
    }
}
