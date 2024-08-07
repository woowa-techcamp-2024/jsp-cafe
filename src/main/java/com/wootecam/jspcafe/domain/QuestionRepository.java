package com.wootecam.jspcafe.domain;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    void save(final Question question);

    int countAll();

    List<Question> findAllOrderByCreatedTimeDesc(final int page, final int size);

    Optional<Question> findById(final Long id);

    void update(final Long id, String editedTitle, String editedContents);

    void deleteById(Long id);
}
