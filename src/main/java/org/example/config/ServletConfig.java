package org.example.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.example.data.ArticleDataHandlerMySql;
import org.example.data.UserDataHandlerMySql;


@WebListener
public class ServletConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        //
        UserDataHandlerMySql userDataHandlerMySql = new UserDataHandlerMySql();
        ArticleDataHandlerMySql articleDataHandlerMySql = new ArticleDataHandlerMySql();

        context.setAttribute(DataHandler.USER.getValue(), userDataHandlerMySql);
        context.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandlerMySql);
    }
}
