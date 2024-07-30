package camp.woowa.jspcafe.models.models;

import camp.woowa.jspcafe.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {
    @Test
    void testQuestion() {
        // given
        Long id = 1L;
        String title = "title";
        String content = "content";
        String writer = "1234";

        // when
        Question question = new Question(id, title, content, writer, 1L);

        // then
        assertEquals(id, question.getId());
        assertEquals(title, question.getTitle());
        assertEquals(content, question.getContent());
        assertEquals(writer, question.getWriter());
    }

    @Test
    void testUpdateQuestion() {
        // given
        Long id = 1L;
        String title = "title";
        String content = "content";
        String writer = "1234";
        Question question = new Question(id, title, content, writer, 1L);
        String updatedTitle = "updatedTitle";
        String updatedContent = "updatedContent";
        // when
        question.update(updatedTitle, updatedContent);

        // then
        assertEquals(question.getTitle(), updatedTitle);
        assertEquals(question.getContent(), updatedContent);
    }
}
