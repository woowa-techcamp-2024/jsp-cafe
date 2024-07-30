package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import camp.woowa.jspcafe.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionRepositoryTest {
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository = new InMemQuestionRepository();
        questionRepository.deleteAll();
    }

    @Test
    void testSave() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long expectedId = 1L;

        // when
        Long id = questionRepository.save(title, content, writer, 1L);

        // then
        assertEquals(expectedId, id);
    }

    @Test
    void testFindAll() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        questionRepository.save(title, content, writer, 1L);

        int expectedSize = 1;

        // when
        List<Question> questions = questionRepository.findAll();

        // then
        assertEquals(expectedSize, questions.size());
    }

    @Test
    void testFindById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionRepository.save(title, content, writer, 1L);

        // when
        Question question = questionRepository.findById(id);

        // then
        assertEquals(title, question.getTitle());
        assertEquals(content, question.getContent());
        assertEquals(writer, question.getWriter());
    }

    @Test
    void testUpdate() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long id = questionRepository.save(title, content, writer, 1L);
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        Question target = questionRepository.findById(id);

        target.update(updatedTitle, updatedContent);

        // when
        questionRepository.update(target);

        // then
        Question question = questionRepository.findById(id);
        assertEquals(updatedTitle, question.getTitle());
        assertEquals(updatedContent, question.getContent());
    }
}
