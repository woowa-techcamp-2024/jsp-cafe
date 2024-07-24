package woopaca.jspcafe.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import woopaca.jspcafe.repository.PostMemoryRepository;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.UserMemoryRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.service.UserService;

@WebListener
public class ApplicationConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("userService", userService());
        servletContext.setAttribute("postService", postService());
    }

    private UserService userService() {
        return new UserService(userRepository());
    }

    private UserRepository userRepository() {
        return new UserMemoryRepository();
    }

    private PostService postService() {
        return new PostService(postRepository(), userRepository());
    }

    private PostRepository postRepository() {
        return new PostMemoryRepository();
    }
}
