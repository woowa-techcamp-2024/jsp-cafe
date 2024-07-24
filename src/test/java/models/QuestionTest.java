package models;

import camp.woowa.jspcafe.models.Question;
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
}
