package com.woowa;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import java.util.Set;

public class DispatcherServletContainer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        Dynamic helloServlet = ctx.addServlet("helloServlet", new DispatcherServlet());
        helloServlet.addMapping("/*");
    }
}
