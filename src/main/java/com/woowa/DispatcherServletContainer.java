package com.woowa;

import com.woowa.database.UserDatabase;
import com.woowa.filter.ErrorHandlingFilter;
import com.woowa.filter.HttpMethodFilter;
import com.woowa.framework.ApplicationInitializer;
import com.woowa.framework.BeanFactory;
import com.woowa.handler.LoginHandler;
import com.woowa.handler.QuestionHandler;
import com.woowa.handler.UserHandler;
import com.woowa.servlet.FindQuestionServlet;
import com.woowa.servlet.ListQuestionServlet;
import com.woowa.servlet.LoginServlet;
import com.woowa.servlet.LogoutServlet;
import com.woowa.servlet.QuestionServlet;
import com.woowa.servlet.SignupServlet;
import com.woowa.servlet.UserProfileServlet;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import java.util.EnumSet;
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

        addServlet(ctx, dispatcherServlet, beanFactory);
        addFilter(ctx);
    }

    private void addServlet(ServletContext ctx, DispatcherServlet dispatcherServlet, BeanFactory beanFactory) {
        Dynamic helloServlet = ctx.addServlet("helloServlet", dispatcherServlet);
        helloServlet.addMapping("/users", "/css/*", "/js/*", "/images/*", "/fonts/*", "/favicon.ico", "/user/*",
                "/qna/*");

        Dynamic signupServlet = ctx.addServlet("signupServlet", new SignupServlet());
        signupServlet.addMapping("/signup");

        Dynamic userProfileServlet = ctx.addServlet("userProfileServlet", new UserProfileServlet(
                beanFactory.getBean(UserHandler.class),
                beanFactory.getBean(UserDatabase.class)));
        userProfileServlet.addMapping("/users/*");

        Dynamic questionServlet = ctx.addServlet("questionServlet",
                new QuestionServlet(beanFactory.getBean(QuestionHandler.class)));
        questionServlet.addMapping("/questions");

        Dynamic loginServlet = ctx.addServlet("loginServlet",
                new LoginServlet(beanFactory.getBean(LoginHandler.class)));
        loginServlet.addMapping("/login");

        Dynamic logoutServlet = ctx.addServlet("logoutServlet",
                new LogoutServlet(beanFactory.getBean(LoginHandler.class)));
        logoutServlet.addMapping("/logout");

        Dynamic listQuestionServlet = ctx.addServlet("listQuestionServlet",
                new ListQuestionServlet(beanFactory.getBean(QuestionHandler.class)));
        listQuestionServlet.addMapping("/");

        Dynamic findQuestionServlet = ctx.addServlet("findQuestionServlet",
                new FindQuestionServlet(beanFactory.getBean(QuestionHandler.class)));
        findQuestionServlet.addMapping("/questions/*");
    }

    private void addFilter(ServletContext ctx) {
        FilterRegistration.Dynamic httpMethodFilter = ctx.addFilter("httpMethodFilter", new HttpMethodFilter());
        httpMethodFilter.addMappingForUrlPatterns(null, true, "/*");

        FilterRegistration.Dynamic errorHandlingFilter = ctx.addFilter("errorHandlingFilter",
                new ErrorHandlingFilter());
        errorHandlingFilter.addMappingForUrlPatterns(null, true, "/*");
    }
}
