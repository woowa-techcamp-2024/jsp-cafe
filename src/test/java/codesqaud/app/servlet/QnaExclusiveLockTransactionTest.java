package codesqaud.app.servlet;

import codesqaud.TestDataSource;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.dao.user.UserDao;
import codesqaud.app.db.TransactionManager;
import codesqaud.app.model.Article;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import codesqaud.mock.MockServletConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QnaExclusiveLockTransactionTest {
    private MockServletConfig config;

    private UserDao userDao;
    private ArticleDao articleDao;
    private ReplyDao replyDao;

    @BeforeEach
    public void setUp() throws SQLException {
        TestDataSource.create();

        config = new MockServletConfig();
        userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
        replyDao = (ReplyDao) config.getServletContext().getAttribute("replyDao");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        TestDataSource.drop();
    }

    //TODO: 작업을 완료 한 후에 테스트하도록 수정
    @Test
    @DisplayName("트랜잭션에서 Article을 배타락으로 조회하면 해당 레코드를 참조해서 Reply를 생성할 수 없다.")
    public void testArticleDeleteTransaction() throws SQLException, InterruptedException {
        //given
        DataSource dataSource = TestDataSource.getDataSource();

        userDao.save(new User("semin", "password", "name1", "choicco1@gmail.com"));
        User user = userDao.findAll().get(0);
        articleDao.save(new Article("title", "contents", user.getId()));
        Article article = articleDao.findAll().get(0);


        //when
        executeDeleteArticleAndReplyAtSameTime(dataSource, article, user);

        //then
        List<Reply> replies = replyDao.findAll();
        Assertions.assertThat(replies.size()).isZero();
    }

    private void executeDeleteArticleAndReplyAtSameTime(DataSource dataSource, Article article, User user) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
                    TransactionManager.executeTransaction(dataSource, () -> {
                        articleDao.findByIdForUpdate(article.getId());
                    });
                }
        );

        executorService.execute(() -> {
                    replyDao.save(new Reply("contents", article.getId(), user.getId()));
                }
        );
    }
}
