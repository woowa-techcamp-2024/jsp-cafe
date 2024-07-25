package org.example.jspcafe.question.repository;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Long save(Question question);

    List<Question> getAll();

    Optional<Question> findById(Long id);
}
