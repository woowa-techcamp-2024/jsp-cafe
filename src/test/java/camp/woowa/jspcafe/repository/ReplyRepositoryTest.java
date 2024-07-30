package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplyRepositoryTest {
    private ReplyRepository replyRepository;

    @BeforeEach
    void setUp() {
        replyRepository = new InMemReplyRepository();
        replyRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        // given
        Long questionId = 1L;
        Long writerId = 1L;
        String writer = "writer";
        String content = "content";

        // when
        Long id = replyRepository.save(new Reply(content, questionId, writer, writerId));
        Reply reply = replyRepository.findById(id);

        // then
        assertEquals(reply.getContent(), content);
        assertEquals(reply.getQuestionId(), questionId);
        assertEquals(reply.getWriter(), writer);
        assertEquals(reply.getWriterId(), writerId);
    }

    @Test
    void testFindByQuestionId() {
        // given
        Long questionId = 1L;
        Long writerId = 1L;
        String writer = "writer";
        String content = "content";
        int iteration = 3;
        for (int i = 0; i < iteration; i++)
            replyRepository.save(new Reply(content, questionId, writer, writerId));


        // when
        List<Reply> reply = replyRepository.findByQuestionId(questionId);

        // then
        assertEquals(iteration, reply.size());
    }
}
