package com.woowa;

import com.woowa.framework.BeanFactory;
import com.woowa.framework.argumentresovler.ArgumentResolverComposite;
import com.woowa.framework.web.HandlerMapping;
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

        HandlerMapping handlerMapping = new HandlerMapping(beanFactory);
        handlerMapping.init();

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMapping);
        dispatcherServlet.init(beanFactory);

        Dynamic helloServlet = ctx.addServlet("helloServlet", dispatcherServlet);
        helloServlet.addMapping("/*");
    }
}
