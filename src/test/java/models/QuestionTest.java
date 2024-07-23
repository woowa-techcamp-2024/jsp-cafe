package models;

import camp.woowa.jspcafe.models.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {
    @Test
    void testQuestion() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        // when
        Question question = new Question(title, content, writer);

        // then
        assertEquals(title, question.getTitle());
        assertEquals(content, question.getContent());
        assertEquals(writer, question.getWriter());
    }
}
