package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Reply;

public interface ReplyRepository {
    Long save(Reply reply);

    Reply findById(Long id);

    void deleteAll();
}
