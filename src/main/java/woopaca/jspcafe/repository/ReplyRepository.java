package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {

    void save(Reply reply);

    List<Reply> findByPostId(Long postId);

    Optional<Reply> findById(Long id);
}
