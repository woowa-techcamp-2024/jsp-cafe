package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
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

    public void update(Long id, String updatedTitle, String updatedContent, long writerId) {
        Question target = findById(id);
        if (!target.getWriterId().equals(writerId)) { // 수정 권한 검증
            throw new CustomException(HttpStatus.FORBIDDEN, "You are not authorized to update this question.");
        }

        target.update(updatedTitle, updatedContent);

        questionRepository.update(target);
    }

    public void deleteById(Long id, long writerId) {
        Question target = findById(id);
        if (!target.getWriterId().equals(writerId)) { // 삭제 권한 검증
            throw new CustomException(HttpStatus.FORBIDDEN, "You are not authorized to delete this question.");
        }

        questionRepository.deleteById(id);
    }
}
