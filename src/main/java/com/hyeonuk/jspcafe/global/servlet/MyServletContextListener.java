package com.hyeonuk.jspcafe.global.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.MysqlArticleDao;
import com.hyeonuk.jspcafe.global.db.DBConnectionInfo;
import com.hyeonuk.jspcafe.global.db.mysql.DBManager;
import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import com.hyeonuk.jspcafe.global.utils.BcryptPasswordEncoder;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.dao.MysqlMemberDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener{
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    private ArticleDao articleDao;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        try {
            DBConnectionInfo connectionInfo = new DBConnectionInfo("application-db.yml");
            DBManager dbManager = new DBManager(connectionInfo);
            memberDao = new MysqlMemberDao(dbManager);
            passwordEncoder = new BcryptPasswordEncoder();
            articleDao = new MysqlArticleDao(dbManager);
            sce.getServletContext().setAttribute("memberDao", memberDao);
            sce.getServletContext().setAttribute("passwordEncoder", passwordEncoder);
            sce.getServletContext().setAttribute("articleDao", articleDao);
        } catch (Exception e) {
            throw new HttpInternalServerErrorException("서버에러입니다.");
        }
    }
}
