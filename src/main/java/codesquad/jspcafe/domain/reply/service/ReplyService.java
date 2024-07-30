package codesquad.jspcafe.domain.reply.service;

import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.reply.payload.request.ReplyCreateRequest;
import codesquad.jspcafe.domain.reply.payload.request.ReplyUpdateRequest;
import codesquad.jspcafe.domain.reply.payload.respose.ReplyCommonResponse;
import codesquad.jspcafe.domain.reply.repository.ReplyRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class ReplyService {

    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    public ReplyService(UserRepository userRepository, ReplyRepository replyRepository) {
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
    }

    public ReplyCommonResponse createReply(ReplyCreateRequest request) {
        Long article = request.getArticle();
        String contents = request.getContents();
        User writer = userRepository.findByUserId(request.getUserId())
            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Reply reply = new Reply(article, writer, contents, LocalDateTime.now());
        return ReplyCommonResponse.from(replyRepository.save(reply));
    }

    public List<ReplyCommonResponse> getRepliesByArticleId(Long articleId) {
        return replyRepository.findByArticleId(articleId).stream()
            .map(ReplyCommonResponse::from)
            .toList();
    }

    public ReplyCommonResponse updateReply(ReplyUpdateRequest request)
        throws AccessDeniedException {
        Reply reply = findReplyById(request.getId());
        if (!reply.getWriter().getUserId().equals(request.getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        reply.update(request.getContents());
        return ReplyCommonResponse.from(replyRepository.update(reply));
    }

    public void deleteReply(Long replyId, String userId) throws AccessDeniedException {
        Reply reply = findReplyById(replyId);
        if (!reply.getWriter().getUserId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        replyRepository.delete(reply);
    }

    private Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
            .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
    }
}
