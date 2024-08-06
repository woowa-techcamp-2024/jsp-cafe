package cafe.config;

import cafe.domain.util.H2Connector;
import cafe.service.ArticleService;
import cafe.service.CommentService;
import cafe.service.SessionService;
import cafe.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DIContainerInitializerTest {
    private static DIContainerInitializer diContainerInitializer;

    @BeforeAll
    static void setUp() {
        diContainerInitializer = new DIContainerInitializer();
    }

    @Test
    void 올바른_속성이_context에_추가된다() {
        ServletContext servletContext = mock(ServletContext.class);
        diContainerInitializer.contextInit(new H2Connector(), servletContext);

        verify(servletContext).setAttribute(eq("userService"), any(UserService.class));
        verify(servletContext).setAttribute(eq("articleService"), any(ArticleService.class));
        verify(servletContext).setAttribute(eq("sessionService"), any(SessionService.class));
        verify(servletContext).setAttribute(eq("commentService"), any(CommentService.class));
    }
}