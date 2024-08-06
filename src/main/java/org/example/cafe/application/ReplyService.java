package org.example.cafe.application;

import java.time.LocalDateTime;
import java.util.List;
import org.example.cafe.application.dto.ReplyCreateDto;
import org.example.cafe.application.dto.ReplyPageParam;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;
import org.example.cafe.domain.ReplyRepository;

public class ReplyService {

    public static final int PAGE_SIZE = 5;

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply createReply(ReplyCreateDto replyCreateDto, String writer) {
        Long savedReplyId = replyRepository.save(replyCreateDto.toReply(writer));
        return new ReplyBuilder()
                .replyId(savedReplyId)
                .questionId(replyCreateDto.questionId())
                .writer(writer)
                .content(replyCreateDto.content())
                .build();
    }

    public List<Reply> findAll(Long questionId) {
        return replyRepository.findByQuestionId(questionId);
    }

    public List<Reply> findReplyPageByQuestionId(ReplyPageParam replyPageParam) {
        ReplyPageDto replyPageDto = new ReplyPageDto(replyPageParam.questionId(),
                replyPageParam.lastReplyId(), replyPageParam.createdAt(), PAGE_SIZE);

        return replyRepository.findByQuestionId(replyPageDto);
    }

    public void deleteReply(Long replyId, String userId) {
        Reply reply = replyRepository.findById(replyId);
        if (reply == null) {
            throw new DataNotFoundException("댓글을 찾을 수 없습니다.");
        }

        validWriter(userId, reply);
        reply.delete();

        replyRepository.update(reply);
    }

    public void validWriter(String loginUserId, Reply reply) {
        if (!reply.hasWriter(loginUserId)) {
            throw new BadAuthenticationException("작성자만 수정, 삭제할 수 있습니다.");
        }
    }

    public record ReplyPageDto(Long questionId,
                               Long lastReplyId,
                               LocalDateTime createdAt,
                               int pageSize) {
    }
}
