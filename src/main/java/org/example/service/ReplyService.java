package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.example.dto.ReplyCreateDto;
import org.example.entity.Reply;
import org.example.exception.NotSameAuthorException;
import org.example.repository.ReplyRepository;
import org.example.repository.ReplyRepositoryDBImpl;

public class ReplyService {

    ReplyRepository replyRepository = ReplyRepositoryDBImpl.getInstance();

    //댓글 생성
    public void createReply(
        ReplyCreateDto replyCreateDto
    ) {
        // 댓글 생성
        Reply reply = replyCreateDto.toEntity();

        // 댓글 저장
        replyRepository.save(reply);
    }

    // 댓글 삭제
    public void deleteReply(
        int replyId,
        String userId
    ) {
        // 댓글 찾기
        Reply reply = replyRepository.findById(replyId).orElseThrow(
            () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if (!reply.isOwner(userId)){
            throw new NotSameAuthorException("작성자만 삭제할 수 있습니다.");
        }

        // 댓글 삭제
        replyRepository.deleteById(replyId);
    }

    // 글에 달린 댓글 찾기
    public List<Reply> findRepliesByArticleId(
        int articleId,
        int start,
        int count
    ) {
        return replyRepository.findAllByArticleId(articleId, start, count);
    }

    public int findReplyCount(Integer articleId) {
        return replyRepository.findReplyCount(articleId);
    }
}
