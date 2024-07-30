package org.example.cafe.application;

import java.util.List;
import org.example.cafe.application.dto.QuestionCreateDto;
import org.example.cafe.application.dto.QuestionUpdateDto;
import org.example.cafe.common.error.BadAuthenticationException;
import org.example.cafe.common.error.DataNotFoundException;
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
        Question question = questionRepository.findById(id);
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        return question;
    }

    public void updateQuestion(String loginUserId, QuestionUpdateDto questionUpdateDto) {
        Question question = questionRepository.findById(questionUpdateDto.questionId());
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        validWriter(loginUserId, question);
        questionRepository.update(questionUpdateDto.toQuestion(loginUserId));
    }

    public void deleteQuestion(String loginUserId, Long id) {
        Question question = questionRepository.findById(id);
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        validWriter(loginUserId, question);
        questionRepository.delete(id);
    }

    public void validWriter(String loginUserId, Question question) {
        if (question.isValidWriter(loginUserId)) {
            throw new BadAuthenticationException("작성자만 수정, 삭제할 수 있습니다.");
        }
    }
}
