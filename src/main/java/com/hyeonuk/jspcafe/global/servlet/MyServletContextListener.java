package com.hyeonuk.jspcafe.global.servlet;

import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener{
    private MemberDao memberDao;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        memberDao = new InMemoryMemberDao();
        sce.getServletContext().setAttribute("memberDao", memberDao);
    }
}
