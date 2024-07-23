package org.example.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.example.data.ArticleDataHandlerInMemory;
import org.example.data.UserDataHandlerInMemory;



@WebListener
public class ServletConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        //
        UserDataHandlerInMemory userDataHandlerInMemory = new UserDataHandlerInMemory();
        ArticleDataHandlerInMemory articleDataHandlerInMemory = new ArticleDataHandlerInMemory();

        context.setAttribute("userDataHandlerInMemory", userDataHandlerInMemory);
        context.setAttribute("articleDataHandlerInMemory", articleDataHandlerInMemory);
    }
}
