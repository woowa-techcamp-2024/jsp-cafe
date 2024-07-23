package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.repository.QuestionRepository;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long save(String title, String content, String writer) {
        return questionRepository.save(title, content, writer);
    }
}
