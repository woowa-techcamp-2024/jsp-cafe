package com.woowa;

import com.woowa.framework.ApplicationInitializer;
import com.woowa.framework.BeanFactory;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServletContainer implements ServletContainerInitializer {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServletContainer.class);

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.start();

        ApplicationInitializer applicationInitializer = new ApplicationInitializer(beanFactory);
        applicationInitializer.init();

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init(beanFactory);

        Dynamic helloServlet = ctx.addServlet("helloServlet", dispatcherServlet);
        helloServlet.addMapping("/users", "/css/*", "/js/*", "/images/*", "/font/*", "/favicon.ico", "/user/*", "/qna/*");
    }
}
