package woowa.camp.jspcafe.service;

import java.time.LocalDateTime;
import java.util.List;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.ReplyResponse;

public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public ReplyService(ReplyRepository replyRepository, UserRepository userRepository,
                        DateTimeProvider dateTimeProvider) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public Reply writeReply(ReplyWriteRequest replyWriteRequest) {
        LocalDateTime currentTime = dateTimeProvider.getNowAsLocalDateTime();

        Reply reply = new Reply(replyWriteRequest.userId(),
                replyWriteRequest.articleId(),
                replyWriteRequest.content(),
                currentTime,
                currentTime,
                null);

        return replyRepository.save(reply);
    }

    public List<ReplyResponse> findReplyList(Long articleId) {
        return replyRepository.findByArticleIdWithUser(articleId);
    }

}
