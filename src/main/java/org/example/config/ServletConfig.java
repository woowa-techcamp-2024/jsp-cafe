package org.example.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.constance.DataHandler;
import org.example.data.ArticleDataHandlerMySql;
import org.example.data.ReplyDataHandler;
import org.example.data.ReplyDataHandlerMySql;
import org.example.data.UserDataHandlerMySql;


@WebListener
public class ServletConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        //
        UserDataHandlerMySql userDataHandlerMySql = new UserDataHandlerMySql();
        ArticleDataHandlerMySql articleDataHandlerMySql = new ArticleDataHandlerMySql();
        ReplyDataHandler replyDataHandler = new ReplyDataHandlerMySql();

        context.setAttribute(DataHandler.USER.getValue(), userDataHandlerMySql);
        context.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandlerMySql);
        context.setAttribute(DataHandler.REPLY.getValue(), replyDataHandler);
    }
}
