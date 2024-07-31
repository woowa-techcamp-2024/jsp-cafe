package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.ReplyRepository;
import com.wootecam.jspcafe.exception.NotFoundException;
import java.time.LocalDateTime;

public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(final ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply append(final Long questionId, final Long userPrimaryId, final String writer, final String contents) {
        Reply reply = new Reply(writer, contents, LocalDateTime.now(), userPrimaryId, questionId);
        Long savedId = replyRepository.save(reply);

        return replyRepository.findById(savedId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }
}
