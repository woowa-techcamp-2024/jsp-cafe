package com.woowa.database;

import com.woowa.model.Question;
import java.util.List;

public interface QuestionDatabase {
    void save(Question question);

    List<Question> findAll();

    List<Question> findAllOrderByCreatedAt(int page, int size);
}
