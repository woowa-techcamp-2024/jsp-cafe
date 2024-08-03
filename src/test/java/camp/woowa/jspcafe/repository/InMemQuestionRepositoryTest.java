package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemQuestionRepositoryTest {
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
        Question save = new Question(title, content, writer, 1L);
        // when
        Long id = questionRepository.save(save);

        // then
        assertEquals(expectedId, id);
    }

    @Test
    void testFindAll() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Question save = new Question(title, content, writer, 1L);
        questionRepository.save(save);

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
        Question save = new Question(title, content, writer, 1L);

        Long id = questionRepository.save(save);;

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
        Question save = new Question(title, content, writer, 1L);

        Long id = questionRepository.save(save);;

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

    @Test
    void testDeleteById() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Question save = new Question(title, content, writer, 1L);

        Long id = questionRepository.save(save);;

        // when
        questionRepository.deleteById(id);

        // then
        assertEquals(0, questionRepository.findAll().size());
    }
}
