package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.repository.QuestionRepository;

import java.util.List;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long save(String title, String content, String writer, Long writerId) {
        return questionRepository.save(title, content, writer, writerId);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id);
    }
}
