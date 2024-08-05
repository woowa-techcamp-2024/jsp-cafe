package woowa.cafe.service;

import woowa.cafe.domain.Reply;
import woowa.cafe.dto.ReplyInfo;
import woowa.cafe.dto.UserInfo;
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
        return replyRepository.findAllByQuestionId(questionId, null).stream()
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

    public ReplyInfo createReply(String questionId, String content, String userId, String authorName) {
        Reply reply = new Reply(content, "ACTIVE", userId, authorName, questionId);
        replyRepository.save(reply);
        return new ReplyInfo(
                reply.getId(),
                reply.getContent(),
                reply.getCreatedDate(),
                reply.getStatus(),
                reply.getUserId(),
                reply.getAuthorName(),
                reply.getQuestionId()
        );
    }

    public boolean deleteReply(UserInfo userInfo, String questionId, String replyId) {
        Reply reply = replyRepository.findById(replyId);

        if (reply == null) {
            return true;
        }

        if (!reply.getUserId().equals(userInfo.id())) {
            return false;
        }
        replyRepository.deleteById(replyId);
        return true;
    }
}
