package codesquad.jspcafe.servlet;

import codesquad.jspcafe.domain.user.service.UserSignService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DefaultServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("userSignService", new UserSignService());
    }
}
