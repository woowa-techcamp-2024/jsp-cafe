package camp.woowa.jspcafe.service;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.repository.ReplyRepository;

import java.util.List;

public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Long createReply(Long questionId, Long writerId, String writer, String content) {
        return replyRepository.save(new Reply(content, questionId, writer, writerId));
    }

    public Reply findById(Long id) {
        return replyRepository.findById(id);
    }

    public List<Reply> findByQuestionId(Long questionId) {
        return replyRepository.findByQuestionId(questionId);
    }

    public void deleteById(Long id, long writerId) {
        Reply reply = replyRepository.findById(id);
        if (reply.getWriterId() != writerId) { // 작성자인지 확인
            throw new CustomException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }
        replyRepository.deleteById(id);
    }
}
