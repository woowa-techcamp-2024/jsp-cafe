package cafe.questions.repository;

import cafe.questions.Reply;

import java.util.List;

public interface ReplyRepository {
    Reply save(Reply reply);

    Reply findById(Long id);

    List<Reply> findByArticleId(Long articleId);

    void deleteById(Long id);
}
