package org.example.reply.service;

import java.sql.SQLException;
import java.util.List;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dto.UserDto;
import org.example.reply.model.dao.Reply;
import org.example.reply.model.dto.ReplyDto;
import org.example.reply.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReplyService {

    public static final int PAGE_SIZE = 5;
    private static final Logger logger = LoggerFactory.getLogger(ReplyService.class);
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public List<ReplyDto> getAllReplies(Long postId) throws SQLException {
        List<ReplyDto> replies = replyRepository.findAll(postId);
        return replies;
    }

    public List<ReplyDto> getAllReplies(Long postId, int page) throws SQLException {
        int offset = (page - 1) * PAGE_SIZE;
        List<ReplyDto> replies = replyRepository.findAll(postId, PAGE_SIZE, offset);
        return replies;
    }

    public ReplyDto getReplyById(Long replyId) throws SQLException {
        ReplyDto replies = replyRepository.findById(replyId);
        return replies;
    }

    public ReplyDto saveReply(UserDto user, Long postId, String contents) throws SQLException {
        Reply reply = Reply.create(user.getUserId(), postId, contents);
        Reply save = replyRepository.save(reply);

        return ReplyDto.toDto(save);
    }

    public ReplyDto updateReply(Long replyId, UserDto user, String contents) throws SQLException {
        ReplyDto reply = replyRepository.findById(replyId); // 존재하는지 확인
        if (!user.getUserId().equals(reply.getUserId())) {
            throw new IllegalArgumentException("자신이 작성한 댓글이 아닙니다.");
        }
        reply.updateContents(contents);
        ReplyDto update = replyRepository.update(reply);
        return update;
    }

    public boolean deleteReply(Long id, UserDto user) throws SQLException {
        ReplyDto reply = replyRepository.findById(id);
        if (reply.getUserId().equals(user.getUserId())) {
            replyRepository.delete(id);
            return true;
        }
        return false;
    }
}
