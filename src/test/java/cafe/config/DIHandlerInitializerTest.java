package cafe.config;

import cafe.controller.handler.articles.*;
import cafe.controller.handler.comments.CommentCreateHandler;
import cafe.controller.handler.comments.CommentDeleteHandler;
import cafe.controller.handler.comments.CommentListHandler;
import cafe.controller.handler.users.*;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DIHandlerInitializerTest {
    private static DIHandlerInitializer diHandlerInitializer;

    @BeforeAll
    static void setUp() {
        diHandlerInitializer = new DIHandlerInitializer();
    }

    @Test
    void 올바른_유저_핸들러가_context에_추가된다() {
        ServletContext servletContext = mock(ServletContext.class);
        diHandlerInitializer.userHandlerInit(servletContext);

        verify(servletContext).setAttribute(eq("userInfoHandler"), any(UserInfoHandler.class));
        verify(servletContext).setAttribute(eq("userInfoListHandler"), any(UserInfoListHandler.class));
        verify(servletContext).setAttribute(eq("userSignUpHandler"), any(UserSignUpHandler.class));
        verify(servletContext).setAttribute(eq("userInfoEditHandler"), any(UserInfoEditHandler.class));
        verify(servletContext).setAttribute(eq("userSignInHandler"), any(UserSignInHandler.class));
        verify(servletContext).setAttribute(eq("userSignOutHandler"), any(UserSignOutHandler.class));
    }

    @Test
    void 올바른_글_핸들러가_context에_추가된다() {
        ServletContext servletContext = mock(ServletContext.class);
        diHandlerInitializer.articleHandlerInit(servletContext);

        verify(servletContext).setAttribute(eq("articleHandler"), any(ArticleHandler.class));
        verify(servletContext).setAttribute(eq("articleListHandler"), any(ArticleListHandler.class));
        verify(servletContext).setAttribute(eq("articleCreateHandler"), any(ArticleCreateHandler.class));
        verify(servletContext).setAttribute(eq("articleUpdateHandler"), any(ArticleUpdateHandler.class));
        verify(servletContext).setAttribute(eq("articleDeleteHandler"), any(ArticleDeleteHandler.class));
    }

    @Test
    void 올바른_댓글_핸들러가_context에_추가된다() {
        ServletContext servletContext = mock(ServletContext.class);
        diHandlerInitializer.commentHandlerInit(servletContext);

        verify(servletContext).setAttribute(eq("commentListHandler"), any(CommentListHandler.class));
        verify(servletContext).setAttribute(eq("commentCreateHandler"), any(CommentCreateHandler.class));
        verify(servletContext).setAttribute(eq("commentDeleteHandler"), any(CommentDeleteHandler.class));
    }
}