package woowa.camp.jspcafe.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.ReplyException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.dto.response.ReplyResponse;
import woowa.camp.jspcafe.repository.reply.ReplyCursor;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.request.ReplyWriteRequest;

public class ReplyService {

    private static final Logger log = LoggerFactory.getLogger(ReplyService.class);

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public ReplyService(ReplyRepository replyRepository, UserRepository userRepository,
                        DateTimeProvider dateTimeProvider) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public ReplyResponse writeReply(ReplyWriteRequest replyWriteRequest) {
        LocalDateTime currentTime = dateTimeProvider.getNowAsLocalDateTime();

        Reply reply = new Reply(replyWriteRequest.userId(),
                replyWriteRequest.articleId(),
                replyWriteRequest.content(),
                currentTime,
                currentTime,
                null);
        replyRepository.save(reply);
        User replier = findReplier(reply.getUserId());

        return new ReplyResponse(reply.getReplyId(), reply.getContent(),
                reply.getUserId(), replier.getNickname(), reply.getCreatedAt());
    }

    public Long findReplyCounts(Long articleId) {
        return replyRepository.findReplyCountsByArticleId(articleId);
    }

    public List<ReplyResponse> findReplyList(Long articleId, Long lastReplyId) {
        log.info("{} 게시글에 대한 댓글 리스트 찾기 시작", articleId);
        ReplyCursor cursor = new ReplyCursor(lastReplyId, 5);
        List<ReplyResponse> byArticleIdWithUser = replyRepository.findByArticleIdWithUser(articleId, cursor);
        log.info("{} 게시글에 대한 댓글 리스트 찾기 종료", articleId);
        return byArticleIdWithUser;
    }

    public void deleteReply(User user, Long articleId, Long replyId) {
        Reply reply = findReply(replyId);
        User replier = findReplier(reply.getUserId());

        validateAuthorization(user, replier);
        validateSameArticle(articleId, reply);

        replyRepository.softDeleteById(reply.getReplyId(), dateTimeProvider.getNowAsLocalDateTime());
    }

    private Reply findReply(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException("Reply not found : " + replyId));
    }

    private User findReplier(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User with id " + userId + " not found"));
    }

    private void validateAuthorization(User user, User replier) {
        String userEmail = user.getEmail();
        String authorEmail = replier.getEmail();
        if (!userEmail.equals(authorEmail)) {
            throw new UnAuthorizationException("게시글 수정은 작성자의 이메일과 동일해야 합니다. %s, %s".formatted(userEmail, authorEmail));
        }
    }

    private void validateSameArticle(Long articleId, Reply reply) {
        if (!Objects.equals(reply.getArticleId(), articleId)) {
            throw new ArticleException("편집하려는 댓글의 게시글 id와 일치하지 않습니다. %s %s".formatted(articleId, reply.getArticleId()));
        }
    }

}
