package codesqaud.app.servlet;

import codesqaud.TestDataSource;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.model.Article;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import codesqaud.mock.MockHttpServletRequest;
import codesqaud.mock.MockHttpServletResponse;
import codesqaud.mock.MockServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static codesqaud.app.AuthenticationManager.getLoginUserOrElseThrow;
import static codesqaud.util.LoginUtils.signupAndLogin;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReplyUseCaseTest {
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

    @Nested
    @DisplayName("답글을 작성할 때")
    class CreateReply {
        @Test
        @DisplayName("답글 내용을 작성하면 댓글을 생성할 수 있다.")
        void given_articleAndReply_when_createReply_then_saveReply() throws ServletException, IOException {
            //given
            String body = "{ \"contents\": \"hello\" }";
            request.setInputStream(body);
            signUpAndLoginAndSaveArticle();
            Article article = articleDao.findAll().get(0);
            request.setRequestURI("/qna/" + article.getId() + "/replies");

            //when
            qnaServlet.doPost(request, response);

            //then
            Reply reply = replyDao.findAll().get(0);
            Assertions.assertThat(reply.getContents()).isEqualTo("hello");
        }
    }

    /**
     * PUT "/qna/{articleId}/replies/{replyId}"
     */
    @Nested
    @DisplayName("답변글을 삭제할 때")
    class UpdateArticle {
        @Test
        @DisplayName("삭제할 답변글이 본인의 답변글이면 삭제에 성공한다.")
        void given_myReply_when_deleteReply_then_canDelete() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article article = articleDao.findAll().get(0);
            User user = getLoginUserOrElseThrow(request);
            saveReply(article, user);
            Reply reply = replyDao.findAll().get(0);

            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ article.getId() + "/replies/" + reply.getId());

            //when
            qnaServlet.doDelete(request, response);

            //then
            assertThat(replyDao.findAll().size()).isZero();
        }

        @Test
        @DisplayName("삭제할 답변글이 본인의 답변이 아니면 삭제가 실패한다.")
        void given_othersReply_when_deleteReply_then_throwException() throws ServletException, IOException {
            //given
            signUpAndLoginAndSaveArticle();
            Article article = articleDao.findAll().get(0);
            User user = getLoginUserOrElseThrow(request);

            signupAndLogin(config, request);
            saveReply(article, user);
            Reply reply = replyDao.findAll().get(0);

            request.setMethod("DELETE");
            request.setRequestURI("/qna/"+ article.getId() + "/replies/" + reply.getId());

            //when
            qnaServlet.doDelete(request, response);

            //then
            assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
            assertThat(replyDao.findAll().size()).isEqualTo(1);
        }
    }

    private void signUpAndLoginAndSaveArticle() {
        signupAndLogin(config, request);
        User loginUser = getLoginUserOrElseThrow(request);
        Article article = new Article("Title", "Content 1", loginUser.getId());
        articleDao.save(article);
    }

    private void saveReply(Article article, User user) {
        Reply reply = new Reply("title", article.getId(), user.getId());
        replyDao.save(reply);
    }
}
