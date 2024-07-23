package repository;

import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionRepositoryTest {
    @Test
    void TestSave() {
        // given
        String title = "title";
        String content = "content";
        String writer = "1234";
        Long expectedId = 1L;

        // when
        Long id = new InMemQuestionRepository().save(title, content, writer);

        // then
        assertEquals(expectedId, id);
    }
}
