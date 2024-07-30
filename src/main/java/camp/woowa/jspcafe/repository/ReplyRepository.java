package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Reply;

import java.util.List;

public interface ReplyRepository {
    Long save(Reply reply);

    Reply findById(Long id);

    void deleteAll();

    List<Reply> findByQuestionId(Long questionId);

    void deleteById(Long id);
}
