package com.wootecam.jspcafe.domain;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    void save(final Question question);

    List<Question> findAll();

    Optional<Question> findById(final Long id);
}
