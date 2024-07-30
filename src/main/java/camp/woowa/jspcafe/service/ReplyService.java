package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.repository.ReplyRepository;

public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Long createReply(Long questionId, Long writerId, String writer, String content) {
        return replyRepository.save(new Reply(content, questionId, writer, writerId));
    }

    public Reply findById(Long id) {
        return replyRepository.findById(id);
    }
}
