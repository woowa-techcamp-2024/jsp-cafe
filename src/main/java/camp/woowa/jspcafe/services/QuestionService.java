package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.models.Question;
import camp.woowa.jspcafe.repository.QuestionRepository;

import java.util.List;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long save(String title, String content, String writer) {
        return questionRepository.save(title, content, writer);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id);
    }
}
