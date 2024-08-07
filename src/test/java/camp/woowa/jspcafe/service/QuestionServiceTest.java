package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.MySQLDatabaseManager;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.model.Question;
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

class QuestionServiceTest {
    static ReplyRepository replyRepository;
    static ReplyService replyService;
    static QuestionRepository questionRepository;
    static QuestionService questionService;
    static UserRepository userRepository;
    static DatabaseManager dm;
    static List<Long> writerId;

    @BeforeAll
    static void setUpAll() {
        dm = new MySQLDatabaseManager();
        replyRepository = new MySQLReplyRepository(dm);
        replyService = new ReplyService(replyRepository);
        questionRepository = new MySQLQuestionRepository(dm);
        questionService = new QuestionService(questionRepository, replyService);
        writerId = new ArrayList<>();

        userRepository = new MySQLUserRepository(dm);
        for (int i = 0; i < 10; i++) {
            writerId.add(userRepository.save(new User("userId" + i, "password", "name" + i, "email")));
        }
    }

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
        replyRepository.deleteAll();
    }

    @AfterAll
    static void tearDownAll() {
        questionRepository.deleteAll();
        replyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSave() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";

        // when
        Long id = questionService.save(title, content, writer, writerId.get(0));
        Question question = questionService.findById(id);

        // then
        assertEquals(title, question.getTitle());
    }

    @Test
    void testFindAll() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        questionService.save(title, content, writer, writerId.get(0));

        int expectedSize = 1;

        // when
        int size = questionService.findAll().size();

        // then
        assertEquals(expectedSize, size);
    }

    @Test
    void testFindById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        // when
        String foundTitle = questionService.findById(id).getTitle();

        // then
        assertEquals(title, foundTitle);
    }

    @Test
    void testFindAllWithPage() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";

        // when
        int iteration = 10;
        for (int i = 0; i < iteration; i++) {
            questionService.save(title, content, writer, writerId.get(0));
        }

        // then
        assertEquals(1, questionService.findAllWithPage(new PageRequest(1, 10)).getCurrentPage());
        assertEquals(iteration, questionService.findAllWithPage(new PageRequest(1, 10)).getContents().size());
        assertEquals(2, questionService.findAllWithPage(new PageRequest(1, 10)).getTotalPage());
    }

    @Test
    void testUpdate() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        // when
        questionService.update(id, updatedTitle, updatedContent, writerId.get(0));

        // then
        assertEquals(updatedTitle, questionService.findById(id).getTitle());
        assertEquals(updatedContent, questionService.findById(id).getContent());
    }

    @Test
    void testUpdate_Forbidden() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        // when
        // then
        assertThrows(CustomException.class, () -> questionService.update(id, updatedTitle, updatedContent, 2L));

    }

    @Test
    void testDeleteById_With_Blank_Replies() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        // when
        questionService.deleteById(id, writerId.get(0));

        // then
        assertEquals(0, questionService.findAll().size());
    }

    @Test
    void testDeleteById_With_My_Replies() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        // when
        questionService.deleteById(id, writerId.get(0));
        replyService.createReply(id, writerId.get(0), "writer", "content1");
        replyService.createReply(id, writerId.get(0), "writer", "content2");
        replyService.createReply(id, writerId.get(0), "writer", "content3");

        // then
        assertEquals(0, questionService.findAll().size());
    }

    @Test
    void testDeleteById_With_Others_Replies() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        // when
        replyService.createReply(id, writerId.get(0), "writer", "content1");
        replyService.createReply(id, writerId.get(1), "writer2", "content2");
        replyService.createReply(id, writerId.get(2), "writer3", "content3");

        // then
        CustomException e = assertThrows(CustomException.class, () -> questionService.deleteById(id, writerId.get(0)));
        assertEquals("You can't delete this question. Because there are replies from other users.", e.getMessage());
    }


    @Test
    void testDeleteById_Forbidden() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, writerId.get(0));

        // when
        // then
        assertThrows(CustomException.class, () -> questionService.deleteById(id, 2L));
    }
}
