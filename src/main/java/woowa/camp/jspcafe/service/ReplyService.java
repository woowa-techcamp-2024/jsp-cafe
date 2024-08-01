package woowa.camp.jspcafe.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.ReplyException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.ReplyResponse;
import woowa.camp.jspcafe.service.dto.ReplyWriteRequest;

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

    public void deleteReply(User user, Long articleId, Long replyId) {
        Reply reply = findReply(replyId);
        User replier = findReplier(reply.getUserId());

        validateAuthorization(user, replier);
        validateSameArticle(articleId, reply);

        replyRepository.softDeleteById(reply.getReplyId(), dateTimeProvider.getNowAsLocalDateTime());
    }

//    // TODO
//    public void updateReply(User user, ReplyUpdateRequest replyUpdateRequest) {
//        Reply reply = findReply(replyUpdateRequest.replyId());
//        User replier = findReplier(user.getId());
//
//        validateAuthorization(user, replier);
//        validateSameArticle(replyUpdateRequest.articleId(), reply);
//
//        Reply updateReply = Reply.update(reply, replyUpdateRequest.content(), dateTimeProvider.getNowAsLocalDateTime());
//        replyRepository.update(updateReply);
//    }

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
