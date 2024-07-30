package org.example.cafe.application;

import org.example.cafe.application.dto.ReplyCreateDto;
import org.example.cafe.domain.ReplyRepository;

public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public void createReply(ReplyCreateDto replyCreateDto, String writer) {
        replyRepository.save(replyCreateDto.toReply(writer));
    }
}
