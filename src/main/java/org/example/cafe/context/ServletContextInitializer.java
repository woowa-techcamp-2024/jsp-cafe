package org.example.cafe.context;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.function.Supplier;
import org.example.cafe.domain.user.UserRepository;

@WebListener
public class ServletContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        registerBean(sce.getServletContext());
    }

    private void registerBean(ServletContext servletContext) {
        setContext(servletContext, "UserRepository", UserRepository::new);
    }

    public void setContext(ServletContext servletContext, String name, Supplier<Object> supplier) {
        servletContext.setAttribute(name, supplier.get());
        System.out.println("setContext: " + name);
    }
}
