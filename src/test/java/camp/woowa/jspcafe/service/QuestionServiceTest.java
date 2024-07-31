package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import camp.woowa.jspcafe.repository.InMemReplyRepository;
import camp.woowa.jspcafe.repository.QuestionRepository;
import camp.woowa.jspcafe.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionServiceTest {
    ReplyRepository replyRepository;
    ReplyService replyService;
    QuestionRepository questionRepository;
    QuestionService questionService;

    @BeforeEach
    void setUp() {
        replyRepository = new InMemReplyRepository();
        replyService = new ReplyService(replyRepository);

        questionRepository = new InMemQuestionRepository();
        questionService = new QuestionService(questionRepository, replyService);
        questionRepository.deleteAll();
        replyRepository.deleteAll();
    }

    @Test
    void testSave() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";

        // when
        Long id = questionService.save(title, content, writer, 1L);
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
        questionService.save(title, content, writer, 1L);

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
        Long id = questionService.save(title, content, writer, 1L);

        // when
        String foundTitle = questionService.findById(id).getTitle();

        // then
        assertEquals(title, foundTitle);
    }

    @Test
    void testUpdate() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, 1L);

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        // when
        questionService.update(id, updatedTitle, updatedContent, 1L);

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
        Long id = questionService.save(title, content, writer, 1L);

        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";

        // when
        // then
        assertThrows(CustomException.class, () -> questionService.update(id, updatedTitle, updatedContent, 2L));

    }

    @Test
    void testDeleteById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, 1L);

        // when
        questionService.deleteById(id, 1L);

        // then
        assertEquals(0, questionService.findAll().size());
    }

    @Test
    void testDeleteById_Forbidden() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, 1L);

        // when
        // then
        assertThrows(CustomException.class, () -> questionService.deleteById(id, 2L));
    }
}
