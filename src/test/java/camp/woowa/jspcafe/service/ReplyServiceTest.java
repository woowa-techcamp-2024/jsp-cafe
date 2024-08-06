package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.MySQLDatabaseManager;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.repository.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReplyServiceTest {
    static ReplyService replyService;
    static ReplyRepository replyRepository;
    static DatabaseManager dm;
    static QuestionRepository questionRepository;
    static UserRepository userRepository;
    static List<Long> writerId;
    static List<Long> questionId;

    @BeforeAll
    static void setUpAll() {
        writerId = new ArrayList<>();
        questionId = new ArrayList<>();

        dm = new MySQLDatabaseManager();
        userRepository = new MySQLUserRepository(dm);
        questionRepository = new MySQLQuestionRepository(dm);

        replyRepository = new MySQLReplyRepository(dm);
        replyService = new ReplyService(replyRepository);

        for (int i = 0; i < 10; i++) {
            writerId.add(userRepository.save(new User("userId" + i, "password", "name" + i, "email")));
            questionId.add(questionRepository.save(new Question("title" + i, "content" + i, "writer" + i, writerId.get(i))));
        }
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
    void testCreateReply() {
        // given
        String writer = "writer";
        String content = "content";

        // when
        Long id = replyService.createReply(questionId.get(0), writerId.get(0), writer, content);
        Reply reply = replyService.findById(id);

        // then
        assertEquals(content, reply.getContent());
        assertEquals(questionId.get(0), reply.getQuestionId());
        assertEquals("userId" + 0, reply.getWriter());
        assertEquals(writerId.get(0), reply.getWriterId());
    }

    @Test
    void testFindByQuestionId() {
        // given
        String writer = "writer";
        String content = "content";
        int iteration = 3;
        for (int i = 0; i < iteration; i++)
            replyService.createReply(questionId.get(0), writerId.get(0), writer, content);

        // when
        int size = replyService.findByQuestionId(questionId.get(0)).size();

        // then
        assertEquals(iteration, size);
    }

    @Test
    void testDeleteById() {
        // given
        String writer = "writer";
        String content = "content";
        Long id = replyService.createReply(questionId.get(0), writerId.get(0), writer, content);

        // when
        replyService.deleteById(id, writerId.get(0));

        // then
        assertEquals(0, replyService.findByQuestionId(questionId.get(0)).size());
    }

    @Test
    void testDeleteById_Forbidden() {
        // given
        String writer = "writer";
        String content = "content";
        Long id = replyService.createReply(questionId.get(0), writerId.get(0), writer, content);

        // when
        // then
        assertThrows(CustomException.class, () -> replyService.deleteById(id, 2L));
    }
}
