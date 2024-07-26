package woowa.camp.jspcafe.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.UserService;

class AppContextListenerTest {

    AppContextListener listener = new AppContextListener();

    @Test
    @DisplayName("[Success] 리스너를 통해 UserRepository를 초기화하고 가져온다")
    void initUserRepository() {
        TestServletContext source = new TestServletContext();
        ServletContextEvent sce = new ServletContextEvent(source);
        listener.contextInitialized(sce);

        Object userRepository = source.getAttribute("userRepository");
        assertThat(userRepository).isNotNull();
        assertThat(userRepository).isInstanceOf(UserRepository.class);
    }

    @Test
    @DisplayName("[Success] 리스너를 통해 UserService를 초기화하고 가져온다")
    void initUserService() {
        TestServletContext source = new TestServletContext();
        ServletContextEvent sce = new ServletContextEvent(source);
        listener.contextInitialized(sce);

        Object userRepository = source.getAttribute("userService");
        assertThat(userRepository).isNotNull();
        assertThat(userRepository).isInstanceOf(UserService.class);
    }

    @Test
    @DisplayName("[Success] 리스너를 통해 ArticleRepository 초기화하고 가져온다")
    void initArticleRepository() {
        TestServletContext source = new TestServletContext();
        ServletContextEvent sce = new ServletContextEvent(source);
        listener.contextInitialized(sce);

        Object userRepository = source.getAttribute("articleRepository");
        assertThat(userRepository).isNotNull();
        assertThat(userRepository).isInstanceOf(ArticleRepository.class);
    }

    @Test
    @DisplayName("[Success] 리스너를 통해 ArticleService 초기화하고 가져온다")
    void initArticleService() {
        TestServletContext source = new TestServletContext();
        ServletContextEvent sce = new ServletContextEvent(source);
        listener.contextInitialized(sce);

        Object userRepository = source.getAttribute("articleService");
        assertThat(userRepository).isNotNull();
        assertThat(userRepository).isInstanceOf(ArticleService.class);
    }

}