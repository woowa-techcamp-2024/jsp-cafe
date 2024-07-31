package com.jspcafe.container;

import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.board.service.ArticleService;
import com.jspcafe.board.service.ReplyService;
import com.jspcafe.user.model.UserDao;
import com.jspcafe.user.service.UserService;
import com.jspcafe.util.DatabaseConnector;
import com.jspcafe.util.MysqlConnector;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        initDatabaseConnector(ctx);
        initUserDao(ctx);
        initUserService(ctx);
        initArticleDao(ctx);
        initArticleService(ctx);
        initReplyDao(ctx);
        initReplyService(ctx);
    }

    private void initDatabaseConnector(ServletContext ctx) {
        ctx.setAttribute("databaseConnector", new MysqlConnector());
    }

    private void initUserDao(ServletContext ctx) {
        ctx.setAttribute("userDao", new UserDao((DatabaseConnector) ctx.getAttribute("databaseConnector")));
    }

    private void initUserService(ServletContext ctx) {
        ctx.setAttribute("userService", new UserService((UserDao) ctx.getAttribute("userDao")));
    }

    private void initArticleDao(ServletContext ctx) {
        ctx.setAttribute("articleDao", new ArticleDao((DatabaseConnector) ctx.getAttribute("databaseConnector")));
    }

    private void initArticleService(ServletContext ctx) {
        ctx.setAttribute("articleService", new ArticleService((ArticleDao) ctx.getAttribute("articleDao")));
    }

    private void initReplyDao(ServletContext ctx) {
        ctx.setAttribute("replyDao", new ReplyDao((DatabaseConnector) ctx.getAttribute("databaseConnector")));
    }

    private void initReplyService(ServletContext ctx) {
        ctx.setAttribute("replyService", new ReplyService((ReplyDao) ctx.getAttribute("replyDao")));
    }
}
