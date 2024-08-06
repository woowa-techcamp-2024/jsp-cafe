package codesqaud.app.dao.reply;

import codesqaud.app.dao.CommonDao;
import codesqaud.app.dto.ReplyDto;
import codesqaud.app.model.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyDao extends CommonDao<Reply, Long> {
    List<Reply> findByArticleId(Long articleId);
    Optional<ReplyDto> findByIdAsDto(Long id);
    List<ReplyDto> findByArticleIdAsDto(Long articleId);
}
