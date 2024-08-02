package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.MySQLDatabaseManager;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MySQLReplyRepositoryTest {
    static ReplyRepository replyRepository;
    static QuestionRepository questionRepository;
    static UserRepository userRepository;

    static DatabaseManager dm;

    static Long questionId;
    static Long userId;

    @BeforeAll
    static void setUpAll() {
        dm = new MySQLDatabaseManager();
        replyRepository = new MySQLReplyRepository(dm);
        questionRepository = new MySQLQuestionRepository(dm);
        userRepository = new MySQLUserRepository(dm);

        userId = userRepository.save(new User("userId", "password", "name", "email"));
        questionId = questionRepository.save(new Question("title", "content", "writer", userId));
    }

    @BeforeEach
    void setUp() {
        replyRepository.deleteAll();
    }

    @AfterAll
    static void tearDownAll() {
        replyRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        // given
        String writer = "writer";
        String content = "content";

        // when
        Long id = replyRepository.save(new Reply(content, questionId, writer, userId));
        Reply reply = replyRepository.findById(id);

        // then
        assertEquals(reply.getContent(), content);
        assertEquals(reply.getQuestionId(), questionId);
        assertEquals(reply.getWriter(), writer);
        assertEquals(reply.getWriterId(), userId);
    }

    @Test
    void testFindByQuestionId() {
        // given
        String writer = "writer";
        String content = "content";
        int iteration = 3;
        for (int i = 0; i < iteration; i++)
            replyRepository.save(new Reply(content, questionId, writer, userId));


        // when
        List<Reply> reply = replyRepository.findByQuestionId(questionId);

        // then
        assertEquals(iteration, reply.size());
    }

    @Test
    void testDeleteById() {
        // given
        String writer = "writer";
        String content = "content";

        // when
        Long id = replyRepository.save(new Reply(content, questionId, writer, userId));
        replyRepository.deleteById(id);

        // then
        assertEquals(0, replyRepository.findByQuestionId(questionId).size());
    }
}
