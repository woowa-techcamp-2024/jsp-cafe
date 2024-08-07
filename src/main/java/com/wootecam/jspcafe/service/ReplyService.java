package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.ReplyRepository;
import com.wootecam.jspcafe.exception.BadRequestException;
import com.wootecam.jspcafe.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public class ReplyService {

    private static final int MINIMUM_REPLY_COUNT = 1;

    private final ReplyRepository replyRepository;

    public ReplyService(final ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply append(final Long questionId, final Long userPrimaryId, final String writer, final String contents) {
        Reply reply = new Reply(writer, contents, LocalDateTime.now(), userPrimaryId, questionId);
        Long savedId = replyRepository.save(reply);

        return read(savedId);
    }

    private Reply read(final Long savedId) {
        return replyRepository.findById(savedId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    public int countAll(final Long questionPrimaryId) {
        return replyRepository.countAll(questionPrimaryId);
    }

    public List<Reply> readAll(final Long questionId, final int readCount) {
        if (readCount < MINIMUM_REPLY_COUNT) {
            String message = String.format("댓글 조회 갯수는 %d개 이상이어야 합니다.", MINIMUM_REPLY_COUNT);
            throw new BadRequestException(message);
        }
        return replyRepository.findAllByQuestionPrimaryIdLimit(questionId, readCount);
    }

    public void delete(final Long replyId, final Long userPrimaryId) {
        Reply read = read(replyId);

        if (!read.isWriter(userPrimaryId)) {
            throw new BadRequestException("자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        replyRepository.delete(replyId);
    }

    public List<Reply> readAllStartsWith(final Long questionId, final Long lastReplyId, final int readCount) {
        if (readCount < MINIMUM_REPLY_COUNT) {
            String message = String.format("댓글 조회 갯수는 %d개 이상이어야 합니다.", MINIMUM_REPLY_COUNT);
            throw new BadRequestException(message);
        }

        return replyRepository.findAllByQuestionPrimaryIdAndStartWith(questionId, lastReplyId, readCount);
    }
}
