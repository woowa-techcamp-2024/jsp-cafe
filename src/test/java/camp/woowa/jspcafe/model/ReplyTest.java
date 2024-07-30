package camp.woowa.jspcafe.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplyTest {
    @Test
    void testConstructor() {
        // given
        Long id = 1L;
        String content = "content";
        Long questionId = 1L;
        Long writerId = 1L;
        String writer = "writer";

        // when
        Reply reply = new Reply(id, content, questionId, writer, writerId);

        // then
        assertEquals(reply.getId(), id);
        assertEquals(reply.getContent(), content);
        assertEquals(reply.getQuestionId(), questionId);
        assertEquals(reply.getWriter(), writer);
        assertEquals(reply.getWriterId(), writerId);
    }
}
