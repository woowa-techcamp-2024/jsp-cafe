package org.example.reply.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
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

    private static final Logger logger = LoggerFactory.getLogger(ReplyService.class);
    private final ReplyRepository replyRepository;

    @Autowired
    public
    ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public List<ReplyDto> getAllReplies(Long postId) throws SQLException {
        List<Reply> replies = replyRepository.findAll(postId);
        return replies.stream()
                .map(ReplyDto::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public ReplyDto saveReply(UserDto user, Long postId, String contents) throws SQLException {
        Reply reply = Reply.create(user.getName(),postId, contents);
        Reply save = replyRepository.save(reply);

        return ReplyDto.toDto(save);
    }

    public ReplyDto updateReply(Long replyId, UserDto user, String contents) throws SQLException {
        Reply reply = replyRepository.findById(replyId); // 존재하는지 확인
        if (!user.getName().equals(reply.getWriter())) {
            throw new IllegalArgumentException("자신이 작성한 댓글이 아닙니다.");
        }
        reply.updateContents(contents);
        Reply update = replyRepository.update(reply);
        return ReplyDto.toDto(update);
    }

    public boolean deleteReply(Long id, UserDto user) throws SQLException {
        Reply reply = replyRepository.findById(id);
        if (reply.getWriter().equals(user.getName())) {
            replyRepository.delete(id);
            return true;
        }
        return false;
    }
}
