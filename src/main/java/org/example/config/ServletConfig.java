package org.example.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.constance.DataHandler;
import org.example.data.ArticleDataHandlerMySql;
import org.example.data.ConnectionProvider;
import org.example.data.DatabaseConnectionManager;
import org.example.data.ReplyDataHandler;
import org.example.data.ReplyDataHandlerMySql;
import org.example.data.UserDataHandlerMySql;


@WebListener
public class ServletConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ConnectionProvider connectionProvider = new DatabaseConnectionManager();
        //
        UserDataHandlerMySql userDataHandlerMySql = new UserDataHandlerMySql(connectionProvider);
        ArticleDataHandlerMySql articleDataHandlerMySql = new ArticleDataHandlerMySql(connectionProvider);
        ReplyDataHandler replyDataHandler = new ReplyDataHandlerMySql(connectionProvider);

        context.setAttribute(DataHandler.USER.getValue(), userDataHandlerMySql);
        context.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandlerMySql);
        context.setAttribute(DataHandler.REPLY.getValue(), replyDataHandler);
    }
}
