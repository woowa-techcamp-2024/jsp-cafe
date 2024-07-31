package woowa.cafe.service;

import woowa.cafe.domain.Question;
import woowa.cafe.dto.QuestionInfo;
import woowa.cafe.dto.UpdateQuestionRequest;
import woowa.cafe.dto.request.CreateQuestionRequest;
import woowa.cafe.repository.QuestionRepository;
import woowa.frame.core.annotation.Component;

import java.util.List;

@Component
public class QnaService {

    private final QuestionRepository questionRepository;

    public QnaService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void createQna(CreateQuestionRequest request) {
        Question question = new Question(request.authorName(), request.title(), request.content(), request.userId());
        questionRepository.save(question);
    }

    public List<QuestionInfo> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(question -> new QuestionInfo(
                        question.getId(),
                        question.getAuthorName(),
                        question.getTitle(),
                        question.getContent()
                ))
                .toList();
    }

    public QuestionInfo getQuestion(String id) {
        Question question = questionRepository.findById(id);

        if (question == null) return null;

        return new QuestionInfo(
                question.getId(),
                question.getAuthorName(),
                question.getTitle(),
                question.getContent()
        );
    }

    public QuestionInfo updateQuestion(UpdateQuestionRequest updateRequest) {
        Question question = questionRepository.findById(updateRequest.id());

        if (question == null) return null;
        if (!question.getUserId().equals(updateRequest.userId())) return null;

        question.setTitle(updateRequest.title());
        question.setContent(updateRequest.content());
        questionRepository.update(question);

        return new QuestionInfo(
                question.getId(),
                question.getAuthorName(),
                question.getTitle(),
                question.getContent()
        );
    }

    public boolean deleteQuestion(String questionId, String userId) {
        Question question = questionRepository.findById(questionId);

        if (question == null) return false;
        if (!question.getUserId().equals(userId)) return false;

        questionRepository.deleteById(questionId);
        return true;
    }
}
