package woopaca.jspcafe.service;

import woopaca.jspcafe.error.BadRequestException;
import woopaca.jspcafe.error.NotFoundException;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.Reply;
import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.ReplyRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.request.WriteReplyRequest;
import woopaca.jspcafe.servlet.dto.response.RepliesResponse;

import java.util.List;
import java.util.Objects;

public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ReplyService(ReplyRepository replyRepository, PostRepository postRepository, UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void writeReply(WriteReplyRequest writeReplyRequest, Authentication authentication) {
        Long postId = writeReplyRequest.postId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new BadRequestException("[ERROR] 삭제된 게시글에는 댓글을 작성할 수 없습니다.");
        }

        validateReplyContent(writeReplyRequest.content());
        Reply reply = new Reply(writeReplyRequest.content(), authentication.principal().getId(), postId);
        replyRepository.save(reply);
    }

    private void validateReplyContent(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new BadRequestException("[ERROR] 댓글 내용을 입력해주세요.");
        }

        if (content.length() > 200) {
            throw new BadRequestException("[ERROR] 댓글 내용은 200자 이하여야 합니다.");
        }
    }

    public List<RepliesResponse> getReplies(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 게시글을 찾을 수 없습니다."));
        if (post.isDeleted()) {
            throw new NotFoundException("[ERROR] 삭제된 게시글입니다.");
        }

        List<Reply> replies = replyRepository.findByPostId(postId);
        return replies.stream()
                .filter(Reply::isPublished)
                .map(reply -> RepliesResponse.of(reply, findWriterNickname(reply)))
                .toList();
    }

    private String findWriterNickname(Reply reply) {
        Long writerId = reply.getWriterId();
        return userRepository.findById(writerId)
                .map(User::getNickname)
                .orElse("사용자");
    }

    public void deleteReply(Long replyId, Authentication authentication) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException("[ERROR] 댓글을 찾을 수 없습니다."));
        if (!authentication.isPrincipal(reply.getWriterId())) {
            throw new BadRequestException("[ERROR] 댓글 작성자만 삭제할 수 있습니다.");
        }

        reply.softDelete();
        replyRepository.save(reply);
    }
}
