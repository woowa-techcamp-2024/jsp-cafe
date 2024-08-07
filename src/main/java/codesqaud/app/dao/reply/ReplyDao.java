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

    /**
     * NO offset 페이지 쿼리를 이용해 빠른 페이지네이션 적용
     *
     * @param articleId
     * @param pointer
     * @param size
     */
    List<ReplyDto> findPageWithPointer(Long articleId, Long pointer, int size);

    long count(Long articleId);
}
