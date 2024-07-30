package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.repository.InMemReplyRepository;
import camp.woowa.jspcafe.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplyServiceTest {
    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        ReplyRepository replyRepository = new InMemReplyRepository();
        replyService = new ReplyService(replyRepository);
    }

    @Test
    void testCreateReply() {
        // given
        Long questionId = 1L;
        Long writerId = 1L;
        String writer = "writer";
        String content = "content";

        // when
        Long id = replyService.createReply(questionId, writerId, writer, content);
        Reply reply = replyService.findById(id);

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
            replyService.createReply(questionId, writerId, writer, content);

        // when
        int size = replyService.findByQuestionId(questionId).size();

        // then
        assertEquals(iteration, size);
    }
}
