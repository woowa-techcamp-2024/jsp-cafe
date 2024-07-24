package services;

import camp.woowa.jspcafe.models.Question;
import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import camp.woowa.jspcafe.repository.QuestionRepository;
import camp.woowa.jspcafe.services.QuestionService;
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
    void TestSave() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";

        // when
        Long id = questionService.save(title, content, writer);
        Question question = questionService.findById(id);

        // then
        assertEquals(title, question.getTitle());
    }

    @Test
    void TestFindAll() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        questionService.save(title, content, writer);

        int expectedSize = 1;

        // when
        int size = questionService.findAll().size();

        // then
        assertEquals(expectedSize, size);
    }

    @Test
    void TestFindById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionService.save(title, content, writer);

        // when
        String foundTitle = questionService.findById(id).getTitle();

        // then
        assertEquals(title, foundTitle);
    }
}
