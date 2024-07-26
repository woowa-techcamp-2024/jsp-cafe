package woowa.cafe.service;

import woowa.cafe.domain.Question;
import woowa.cafe.dto.QuestionInfo;
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
        Question question = new Question(request.authorName(), request.title(), request.content());
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

        if(question == null) return null;

        return new QuestionInfo(
                question.getId(),
                question.getAuthorName(),
                question.getTitle(),
                question.getContent()
        );
    }
}
