package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import camp.woowa.jspcafe.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionServiceTest {
    QuestionRepository questionRepository;
    QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionRepository = new InMemQuestionRepository();
        questionService = new QuestionService(questionRepository);

        questionRepository.deleteAll();
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
    void testDeleteById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer, 1L);

        // when
        questionService.deleteById(id);

        // then
        assertEquals(0, questionService.findAll().size());
    }
}
