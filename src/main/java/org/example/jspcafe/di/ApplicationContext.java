package org.example.jspcafe;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.jspcafe.di.SimpleDIContainer;

@WebListener
public class ApplicationContext implements ServletContextListener {

    private static SimpleDIContainer container;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            container = new SimpleDIContainer("org.example.jspcafe");
            sce.getServletContext().setAttribute("diContainer", container);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed
    }

    public static SimpleDIContainer getContainer() {
        return container;
    }
}
