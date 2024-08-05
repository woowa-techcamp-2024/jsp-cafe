package org.example.jspcafe.reply.repository;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.reply.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    Long save(Reply reply);
    List<Reply> getAllByQuestionId(Long questionId);

    Optional<Reply> findById(Long id);

    void deleteById(Long replyId);
}
