package codesquad.jspcafe.domain.reply.repository;

import codesquad.jspcafe.domain.reply.domain.Reply;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository {

    Reply save(Reply reply);

    Reply update(Reply reply);

    Optional<Reply> findById(Long id);

    List<Reply> findByArticleId(Long articleId);

    List<Reply> findByArticleId(Long articleId, Long replyId);

    Long delete(Reply reply);
}
