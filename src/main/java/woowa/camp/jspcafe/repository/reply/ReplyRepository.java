package woowa.camp.jspcafe.repository.reply;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.service.dto.ReplyResponse;

public interface ReplyRepository {

    Reply save(Reply reply);

    Optional<Reply> findById(Long id);

    List<Reply> findByArticleId(Long articleId);

    List<ReplyResponse> findByArticleIdWithUser(Long articleId);

    void softDeleteByUserId(Long userId, LocalDateTime deletedTime);

    void softDeleteById(Long id, LocalDateTime deletedTime);

    void update(Reply reply);

}