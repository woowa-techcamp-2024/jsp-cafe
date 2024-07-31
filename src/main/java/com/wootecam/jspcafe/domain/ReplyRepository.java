package com.wootecam.jspcafe.domain;

import java.util.Optional;

public interface ReplyRepository {

    Long save(Reply reply);

    Optional<Reply> findById(Long id);
}
