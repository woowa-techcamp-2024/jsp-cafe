package codesqaud.app.servlet;

import codesqaud.app.dao.ArticleDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import codesqaud.mock.MockHttpServletRequest;
import codesqaud.mock.MockHttpServletResponse;
import codesqaud.mock.MockRequestDispatcher;
import codesqaud.mock.MockServletConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class QnaServletTest {
    private QnaServlet qnaServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletConfig config;
    private ArticleDao articleDao;

    private Article article = new Article("Title", "Contents", "authorId");

    @BeforeEach
    void setUp() throws ServletException {
        qnaServlet = new QnaServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        config = new MockServletConfig();
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
        qnaServlet.init(config);
    }

    @Nested
    class QnaListTest {

        @Test
        void 질문_글_목록_조회() throws ServletException, IOException {
            // Given
            articleDao.save(new Article("Title 1", "Content 1", "author1"));
            articleDao.save(new Article("Title 2", "Content 2", "author2"));
            request.setRequestURI("/");

            // When
            qnaServlet.doGet(request, response);

            // Then
            List<Article> articles = (List<Article>) request.getAttribute("articles");
            assertThat(articles).isNotNull();
            assertThat(articles.size()).isEqualTo(2);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/index.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    class QnaDetailsTest {
        @Test
        void 질문_글_상세_조회_성공() throws ServletException, IOException {
            // Given
            articleDao.save(article);
            Article savedArticle = articleDao.findAll().get(0);
            request.setRequestURI("/qna/" + savedArticle.getId());

            // When
            qnaServlet.doGet(request, response);

            // Then
            Article retrievedArticle = (Article) request.getAttribute("article");
            assertThat(retrievedArticle).isNotNull();
            assertThat(retrievedArticle.getId()).isEqualTo(savedArticle.getId());

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/qna/show.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }

        @Test
        void 존재하지_않는_질문_글_상세_조회() {
            // Given
            request.setRequestURI("/qna/999");

            // When & Then
            assertThatThrownBy(() -> qnaServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Nested
    class ArticleFormTest {

        @Test
        void 질문_글_작성_폼_조회() throws ServletException, IOException {
            request.setRequestURI("/qna/form");

            qnaServlet.doGet(request, response);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/qna/form.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }

    @Nested
    class ArticleCreateTest {
        @Test
        void 질문_글_작성_성공() throws ServletException, IOException {
            request.setRequestURI("/qna/form");
            request.setParameter("authorId", "authorId");
            request.setParameter("title", "New Title");
            request.setParameter("contents", "New Content");

            qnaServlet.doPost(request, response);

            List<Article> articles = articleDao.findAll();

            assertThat(articles).isNotNull();
            assertThat(articles.size()).isEqualTo(1);

            Article newArticle = articles.get(0);
            assertThat(newArticle.getTitle()).isEqualTo("New Title");
            assertThat(newArticle.getContents()).isEqualTo("New Content");

            assertThat(response.getRedirectedUrl()).isEqualTo("/");
        }
    }
}
