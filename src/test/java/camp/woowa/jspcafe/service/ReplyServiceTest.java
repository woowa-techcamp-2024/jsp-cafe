package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.repository.InMemReplyRepository;
import camp.woowa.jspcafe.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplyServiceTest {
    private ReplyRepository replyRepository;
    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        replyRepository = new InMemReplyRepository();
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

        // then
        assertEquals(replyRepository.findById(id).getContent(), content);
        assertEquals(replyRepository.findById(id).getQuestionId(), questionId);
        assertEquals(replyRepository.findById(id).getWriter(), writer);
        assertEquals(replyRepository.findById(id).getWriterId(), writerId);
    }
}
