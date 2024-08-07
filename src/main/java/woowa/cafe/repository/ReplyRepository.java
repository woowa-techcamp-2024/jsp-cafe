package woowa.cafe.repository;

import woowa.cafe.domain.Reply;
import woowa.cafe.dto.Offset;

import java.util.List;

public interface ReplyRepository {
    void save(Reply reply);
    List<Reply> findAll();
    List<Reply> findAllByQuestionId(String questionId, Offset pageable);
    Reply findById(String id);
    void deleteById(String id);
    void deleteByQuestionId(String questionId);
    long count(String questionId);

    boolean existsByQuestionIdAndNotUserId(String questionId, String userId);
}
