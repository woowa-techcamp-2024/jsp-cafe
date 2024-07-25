package com.hyeonuk.jspcafe.global.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.global.utils.BcryptPasswordEncoder;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener{
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    private ArticleDao articleDao;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        memberDao = new InMemoryMemberDao();
        passwordEncoder = new BcryptPasswordEncoder();
        articleDao = new InMemoryArticleDao();
        sce.getServletContext().setAttribute("memberDao", memberDao);
        sce.getServletContext().setAttribute("passwordEncoder", passwordEncoder);
        sce.getServletContext().setAttribute("articleDao", articleDao);

    }
}
