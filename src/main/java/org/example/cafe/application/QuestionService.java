package org.example.cafe.application;

import java.util.List;
import org.example.cafe.application.dto.QuestionCreateDto;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;

public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void createQuestion(QuestionCreateDto questionCreateDto) {
        Question question = questionCreateDto.toQuestion();
        questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id);
    }
}
