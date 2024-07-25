package cafe;

import cafe.questions.QuestionServlet;
import cafe.questions.QuestionWriteServlet;
import cafe.questions.QuestionsServlet;
import cafe.users.UserEditServlet;
import cafe.users.UserRegisterServlet;
import cafe.users.UsersProfileServlet;
import cafe.users.UsersServlet;
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

        addServlet(sc, UserRegisterServlet::new);
        addServlet(sc, () -> new UsersServlet(factory.userRepository()));
        addServlet(sc, () -> new UsersProfileServlet(factory.userRepository()));
        addServlet(sc, () -> new UserEditServlet(factory.userRepository()));

        addServlet(sc, () -> new QuestionWriteServlet(factory.articleRepository()));
        addServlet(sc, () -> new QuestionsServlet(factory.articleRepository()));
        addServlet(sc, () -> new QuestionServlet(factory.articleRepository()));
    }

    private void addServlet(ServletContext sc, Supplier<MappingHttpServlet> servletSupplier) {
        MappingHttpServlet servlet = servletSupplier.get();
        sc.addServlet(servlet.getClass().getName(), servlet).addMapping(servlet.mappings().toArray(String[]::new));
    }
}
