package cafe;

import cafe.filter.ErrorHandlingFilter;
import cafe.filter.MappingFilter;
import cafe.questions.servlet.QuestionServlet;
import cafe.questions.servlet.QuestionWriteServlet;
import cafe.questions.servlet.QuestionsServlet;
import cafe.users.servlet.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.function.Supplier;

@WebListener
public class AppContextListener implements ServletContextListener {
    private final Factory factory;

    public AppContextListener() {
        this(new Factory());
    }

    protected AppContextListener(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        addFilter(sc, ErrorHandlingFilter::new);

        addServlet(sc, UserRegisterServlet::new);
        addServlet(sc, () -> new UsersServlet(factory.userRepository()));
        addServlet(sc, () -> new UsersProfileServlet(factory.userRepository()));
        addServlet(sc, () -> new UserEditServlet(factory.userRepository()));
        addServlet(sc, () -> new UserLoginServlet(factory.userRepository()));
        addServlet(sc, UserLogoutServlet::new);

        addServlet(sc, () -> new QuestionWriteServlet(factory.articleRepository()));
        addServlet(sc, () -> new QuestionsServlet(factory.articleRepository()));
        addServlet(sc, () -> new QuestionServlet(factory.articleRepository(), factory.userRepository()));
    }

    private void addServlet(ServletContext sc, Supplier<MappingHttpServlet> servletSupplier) {
        MappingHttpServlet servlet = servletSupplier.get();
        sc.addServlet(servlet.getClass().getName(), servlet).addMapping(servlet.mappings().toArray(String[]::new));
    }

    private void addFilter(ServletContext sc, Supplier<MappingFilter> filterSupplier) {
        MappingFilter filter = filterSupplier.get();
        sc.addFilter(filter.getClass().getName(), filter).addMappingForUrlPatterns(null, true, filter.mappings().toArray(String[]::new));
    }
}
