package woowa.cafe.service;

import woowa.cafe.domain.Reply;
import woowa.cafe.dto.ReplyInfo;
import woowa.cafe.repository.ReplyRepository;
import woowa.frame.core.annotation.Component;

import java.util.List;

@Component
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public List<ReplyInfo> getAllReplies(String questionId) {
        return replyRepository.findAllByQuestionId(questionId).stream()
                .map(reply -> new ReplyInfo(
                        reply.getId(),
                        reply.getContent(),
                        reply.getCreatedDate(),
                        reply.getStatus(),
                        reply.getUserId(),
                        reply.getAuthorName(),
                        reply.getQuestionId()
                ))
                .toList();
    }

    public void createReply(String questionId, String content, String userId, String authorName) {
        replyRepository.save(new Reply(content, "ACTIVE", userId, authorName, questionId));
    }
}
