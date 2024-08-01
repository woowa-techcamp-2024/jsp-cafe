package com.hyeonuk.jspcafe.global.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.MysqlArticleDao;
import com.hyeonuk.jspcafe.global.db.DBConnectionInfo;
import com.hyeonuk.jspcafe.global.db.DBManager;
import com.hyeonuk.jspcafe.global.db.DBManagerIml;
import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import com.hyeonuk.jspcafe.global.utils.BcryptPasswordEncoder;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.dao.MysqlMemberDao;
import com.hyeonuk.jspcafe.reply.dao.MysqlReplyDao;
import com.hyeonuk.jspcafe.reply.dao.ReplyDao;
import com.hyeonuk.jspcafe.utils.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener{
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    private ArticleDao articleDao;
    private ReplyDao replyDao;
    private ObjectMapper objectMapper;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        try {
            DBConnectionInfo connectionInfo = new DBConnectionInfo("application-db.yml");
            DBManager dbManager = new DBManagerIml(connectionInfo);
            memberDao = new MysqlMemberDao(dbManager);
            passwordEncoder = new BcryptPasswordEncoder();
            articleDao = new MysqlArticleDao(dbManager);
            replyDao = new MysqlReplyDao(dbManager);
            objectMapper = new ObjectMapper();
            sce.getServletContext().setAttribute("memberDao", memberDao);
            sce.getServletContext().setAttribute("passwordEncoder", passwordEncoder);
            sce.getServletContext().setAttribute("articleDao", articleDao);
            sce.getServletContext().setAttribute("replyDao", replyDao);
            sce.getServletContext().setAttribute("objectMapper", objectMapper);
        } catch (Exception e) {
            throw new HttpInternalServerErrorException("서버에러입니다.");
        }
    }
}
