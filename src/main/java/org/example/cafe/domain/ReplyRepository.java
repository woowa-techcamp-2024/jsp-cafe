package org.example.cafe.domain;

import java.util.List;

public interface ReplyRepository {

    Long save(Reply reply);

    Reply findById(Long id);

    List<Reply> findByQuestionId(Long questionId);

    void update(Reply reply);

    void deleteAll();
}
