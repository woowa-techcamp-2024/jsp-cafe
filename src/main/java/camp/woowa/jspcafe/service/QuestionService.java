package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.repository.QuestionRepository;

import java.util.List;

public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ReplyService replyService;

    public QuestionService(QuestionRepository questionRepository, ReplyService replyService) {
        this.questionRepository = questionRepository;
        this.replyService = replyService;
    }

    public Long save(String title, String content, String writer, Long writerId) {
        return questionRepository.save(new Question(title, content, writer, writerId));
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

        if (!isAvailableToDelete(target)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "You can't delete this question. Because there are replies from other users.");
        }
        questionRepository.deleteById(id);
    }

    private boolean isAvailableToDelete(Question question) {
        List<Reply> replies = replyService.findByQuestionId(question.getId());

        // 댓글이 없는 경우 || 자신의 댓글만 있는 경우 삭제 가능
        return replies.isEmpty() || replies.stream().allMatch(reply -> reply.getWriterId().equals(question.getWriterId()));
    }
}
