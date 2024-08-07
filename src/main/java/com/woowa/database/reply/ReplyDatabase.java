package com.woowa.database.reply;

import com.woowa.database.Page;
import com.woowa.model.Reply;
import java.util.List;
import java.util.Optional;

public interface ReplyDatabase {
    void save(Reply reply);

    List<Reply> findAll();

    Optional<Reply> findById(String replyId);

    void delete(Reply reply);

    Page<Reply> findAllByQuestionId(String questionId, int page, int size);
}
