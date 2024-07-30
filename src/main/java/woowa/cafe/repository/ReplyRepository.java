package woowa.cafe.repository;

import woowa.cafe.domain.Reply;

import java.util.List;

public interface ReplyRepository {
    void save(Reply reply);
    List<Reply> findAll();
    Reply findById(String id);
    void deleteById(String id);
    void deleteByQuestionId(String questionId);
}