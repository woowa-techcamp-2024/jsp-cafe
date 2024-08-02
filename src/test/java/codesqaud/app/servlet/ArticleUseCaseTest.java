package codesqaud.app.servlet;

import codesqaud.TestDataSource;
import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import codesqaud.mock.MockHttpServletRequest;
import codesqaud.mock.MockHttpServletResponse;
import codesqaud.mock.MockRequestDispatcher;
import codesqaud.mock.MockServletConfig;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static codesqaud.util.LoginUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ArticleUseCaseTest {
    private QnaServlet qnaServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletConfig config;
    private ArticleDao articleDao;
    private ReplyDao replyDao;

    @BeforeEach
    void setUp() throws ServletException, SQLException {
        TestDataSource.create();

        qnaServlet = new QnaServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        config = new MockServletConfig();
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
        replyDao = (ReplyDao) config.getServletContext().getAttribute("replyDao");
        qnaServlet.init(config);
    }

    @AfterEach
    void destroy() {
        TestDataSource.drop();
    }

    /**
     * GET "/"
     */
    @Nested
    @DisplayName("루트 페이지로 접속할 때")
    class QnaListTest {
        @Test
        @DisplayName("저장된 게시글 목록을 조회할 수 있다")
        void given_saveArticles_when_getRootPage_then_foundArticles() throws ServletException, IOException {
            signupAndLogin(config, request);
            User loginUser = getLoginUser();
            articleDao.save(new Article("Title 1", "Content 1", loginUser.getId()));
            articleDao.save(new Article("Title 2", "Content 2", loginUser.getId()));
            request.setRequestURI("/");

            // When
            qnaServlet.doGet(request, response);

            // Then
            List<ArticleDto> articles = (List<ArticleDto>) request.getAttribute("articles");
            assertThat(articles).isNotNull();
            assertThat(articles.size()).isEqualTo(2);

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/index.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }
    }


    /**
     * GET "/qna/{qnaId}"
     */
    @Nested
    @DisplayName("질문 글 상세 조회 할 때")
    class QnaDetailsTest {
        @Test
        @DisplayName("저장된 질문 글의 id를 통해 게시글을 조회할 수 있다")
        void given_savedArticleId_when_getQnaDetails_then_foundQnaDetails() throws ServletException, IOException {
            // Given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            request.setRequestURI("/qna/" + savedArticle.getId());

            // When
            qnaServlet.doGet(request, response);

            // Then
            ArticleDto articleAttribute = (ArticleDto) request.getAttribute("article");
            assertThat(articleAttribute).isNotNull();
            assertThat(articleAttribute.getId()).isEqualTo(savedArticle.getId());

            MockRequestDispatcher dispatcher = request.getRequestDispatcher();
            assertThat(dispatcher.getForwardedPath()).isEqualTo("/WEB-INF/qna/show.jsp");
            assertThat(dispatcher.getForwardCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("저장되지 않은 질문 글의 id를 통해 조회하면 예외가 발생한다.")
        void given_notExistingArticleId_when_getQnaDetails_then_throwException() throws ServletException, IOException {
            // Given
            request.setRequestURI("/qna/999");

            // When & Then
            assertThatThrownBy(() -> qnaServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    /**
     * GET "/qna/form"
     */
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

    /**
     * GET "/qna/{id}/form"
     */
    @Nested
    @DisplayName("수정 폼을 조회할 때")
    class UpdateArticleFormTest {
        @Test
        @DisplayName("수정할 게시글이 본인의 게시글이면 수정 폼을 조회할 수 있다.")
        void given_myArticle_when_updateForm_then_canAccess() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            request.setRequestURI("/qna/"+ savedArticle.getId() +"/form");

            //when
            qnaServlet.doGet(request, response);

            //then
            Article article = (Article) request.getAttribute("article");
            assertThat(article.getTitle()).isEqualTo("Title");
        }

        @Test
        @DisplayName("수정할 게시글이 본인의 게시글이 아니면 조회할 때 예외가 발생한다.")
        void given_othersArticle_when_requestUpdateForm_then_throwException() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            signupAndLogin(config,request);
            Article savedArticle = articleDao.findAll().get(0);
            request.setRequestURI("/qna/"+ savedArticle.getId() +"/form");

            //when, then
            assertThatThrownBy(() -> qnaServlet.doGet(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    /**
     * POST "/qna"
     */
    @Nested
    @DisplayName("질문 글을 작성할 때")
    class ArticleCreateTest {
        @Test
        @DisplayName("글 정보가 전달되면 게시글이 저장된다.")
        void given_article_when_CreateArticle_then_createdArticle() throws ServletException, IOException {
            //given
            signupAndLogin(config, request);
            request.setRequestURI("/qna");
            request.setParameter("title", "New Title");
            request.setParameter("contents", "New Content");

            //when
            qnaServlet.doPost(request, response);

            //then
            List<Article> articles = articleDao.findAll();
            Article newArticle = articles.get(0);
            assertThat(newArticle.getTitle()).isEqualTo("New Title");
            assertThat(newArticle.getContents()).isEqualTo("New Content");

            assertThat(response.getRedirectedUrl()).isEqualTo("/");
        }

        @Test
        @DisplayName("로그인하지 않으면 예외가 발생한다.")
        void given_articleWithoutAuth_when_createArticle_then_throwException() throws ServletException, IOException {
            request.setRequestURI("/qna");
            request.setParameter("title", "New Title");
            request.setParameter("contents", "New Content");

            assertThatThrownBy(() -> qnaServlet.doPost(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    /**
     * PUT "/qna/{id}"
     */
    @Nested
    @DisplayName("QNA 게시글을 수정할 때")
    class UpdateArticle {
        @Test
        @DisplayName("수정할 게시글이 본인의 게시글이면 수정에 성공한다.")
        void given_myArticle_when_updateArticle_then_canUpdate() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            request.setMethod("PUT");
            request.setRequestURI("/qna/"+ savedArticle.getId());
            request.setParameter("title", "New Title");
            request.setParameter("contents", "New Contents");

            //when
            qnaServlet.doPut(request, response);

            //then
            Article updatedArticle = articleDao.findById(savedArticle.getId()).get();
            assertThat(updatedArticle.getTitle()).isEqualTo("New Title");
        }

        @Test
        @DisplayName("수정할 게시글이 본인의 게시글이 아니면 예외가 발생한다.")
        void given_othersArticle_when_updateArticle_then_throwException() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            signupAndLogin(config,request);
            Article savedArticle = articleDao.findAll().get(0);
            request.setRequestURI("/qna/"+ savedArticle.getId());

            //when, then
            assertThatThrownBy(() -> qnaServlet.doPut(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    /**
     * DELETE "/qna/{id}"
     */
    @Nested
    @DisplayName("QNA 게시글을 삭제할 때")
    class DeleteArticle {
        @Test
        @DisplayName("삭제할 게시글이 본인의 게시글이면 삭제에 성공한다.")
        void given_myArticle_when_deleteArticle_then_canDelete() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ savedArticle.getId());

            //when
            qnaServlet.doDelete(request, response);

            //then
            Optional<Article> deletedArticle = articleDao.findById(savedArticle.getId());
            assertThat(deletedArticle).isEmpty();
        }

        @Test
        @DisplayName("삭제할 게시글에 본인의 댓글만 작성되어 있으면 삭제할 수 있다.")
        void given_myArticleAndMyReplies_when_deleteArticle_then_canDelete() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            saveReply(savedArticle, AuthenticationManager.getLoginUserOrElseThrow(request));
            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ savedArticle.getId());

            //when
            qnaServlet.doDelete(request, response);

            //then
            Optional<Article> deletedArticle = articleDao.findById(savedArticle.getId());
            assertThat(deletedArticle).isEmpty();
        }

        @Test
        @DisplayName("삭제할 게시글에 다른 사람의 댓글이 작성되어 있으면 삭제할 수 없다.")
        void given_myArticleAndOthersReplies_when_deleteArticle_then_throwException() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article savedArticle = articleDao.findAll().get(0);
            User loginUser = AuthenticationManager.getLoginUserOrElseThrow(request);

            signupAndLogin(config,request);
            saveReply(savedArticle, AuthenticationManager.getLoginUserOrElseThrow(request));

            request.getSession().setAttribute("loginUser", loginUser);

            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ savedArticle.getId());

            //when
            assertThatThrownBy(() -> qnaServlet.doDelete(request, response))
                    .isInstanceOf(HttpException.class);
        }

        @Test
        @DisplayName("삭제할 게시글이 본인의 게시글이 아니면 예외가 발생한다.")
        void given_othersArticle_when_updateArticle_then_throwException() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            signupAndLogin(config,request);
            Article savedArticle = articleDao.findAll().get(0);
            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ savedArticle.getId());

            //when, then
            assertThatThrownBy(() -> qnaServlet.doDelete(request, response))
                    .isInstanceOf(HttpException.class);
        }
    }

    private void signUpAndLoginAndSaveArticle() {
        signupAndLogin(config, request);
        User loginUser = getLoginUser();
        Article article = new Article("Title", "Content 1", loginUser.getId());
        articleDao.save(article);
    }

    private void saveReply(Article article, User user) {
        Reply reply = new Reply("title", article.getId(), user.getId());
        replyDao.save(reply);
    }
}