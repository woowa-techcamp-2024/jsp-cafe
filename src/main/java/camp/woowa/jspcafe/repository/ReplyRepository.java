package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.model.Reply;

public interface ReplyRepository {
    public Long save(Reply reply);

    public Reply findById(Long id);

    void deleteAll();
}
