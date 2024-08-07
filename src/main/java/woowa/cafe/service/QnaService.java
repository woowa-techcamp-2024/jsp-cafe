package woowa.cafe.service;

import woowa.cafe.domain.Question;
import woowa.cafe.dto.Pageable;
import woowa.cafe.dto.QuestionInfo;
import woowa.cafe.dto.UpdateQuestionRequest;
import woowa.cafe.dto.request.CreateQuestionRequest;
import woowa.cafe.repository.QuestionRepository;
import woowa.cafe.repository.ReplyRepository;
import woowa.frame.core.annotation.Component;
import woowa.frame.web.collection.Page;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class QnaService {

    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    public QnaService(QuestionRepository questionRepository, ReplyRepository replyRepository) {
        this.questionRepository = questionRepository;
        this.replyRepository = replyRepository;
    }

    public void createQna(CreateQuestionRequest request) {
        Question question = new Question(request.authorName(), request.title(), request.content(), request.userId(), "ACTIVE", LocalDateTime.now());
        questionRepository.save(question);
    }

    public Page<QuestionInfo> getQuestions(Pageable pageable) {
        List<QuestionInfo> questions = questionRepository.findAll(pageable).stream()
                .map(question -> new QuestionInfo(
                        question.getId(),
                        question.getAuthorName(),
                        question.getTitle(),
                        question.getContent(),
                        question.getCreatedAt()
                ))
                .toList();
        long totalCount = questionRepository.count();
        return new Page<>(questions, pageable.size(), totalCount);
    }

    public QuestionInfo getQuestion(String id) {
        Question question = questionRepository.findById(id);

        if (question == null) return null;

        return new QuestionInfo(
                question.getId(),
                question.getAuthorName(),
                question.getTitle(),
                question.getContent(),
                question.getCreatedAt()
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
                question.getContent(),
                question.getCreatedAt()
        );
    }

    public boolean deleteQuestion(String questionId, String userId) {
        Question question = questionRepository.findById(questionId);

        if (question == null) return false;
        if (!question.getUserId().equals(userId)) return false;

        if (replyRepository.existsByQuestionIdAndNotUserId(questionId, userId)) {
            return false;
        } else {
            questionRepository.deleteById(questionId);
            replyRepository.deleteByQuestionId(questionId);
            return true;
        }
    }
}
