package org.example.cafe.application;

import java.util.List;
import org.example.cafe.application.dto.QuestionCreateDto;
import org.example.cafe.application.dto.QuestionUpdateDto;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.BadRequestException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.ReplyRepository;

public class QuestionService {

    public static final int PAGE_SIZE = 15;

    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    public QuestionService(QuestionRepository questionRepository,
                           ReplyRepository replyRepository) {
        this.questionRepository = questionRepository;
        this.replyRepository = replyRepository;
    }

    public void createQuestion(QuestionCreateDto questionCreateDto) {
        Question question = questionCreateDto.toQuestion();
        questionRepository.save(question);
    }

    public List<Question> findAll(Long page) {
        return questionRepository.findAll(page, PAGE_SIZE);
    }

    public Question findById(Long id) {
        Question question = questionRepository.findById(id);
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        return question;
    }

    public void updateQuestion(String loginUserId, QuestionUpdateDto questionUpdateDto) {
        Question question = questionRepository.findById(questionUpdateDto.questionId());
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        validWriter(loginUserId, question);
        questionRepository.update(questionUpdateDto.toQuestion(loginUserId));
    }

    public void deleteQuestion(String loginUserId, Long questionId) {
        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new DataNotFoundException("게시글을 찾을 수 없습니다.");
        }

        validWriter(loginUserId, question);

        List<Reply> replies = replyRepository.findByQuestionId(questionId);
        validNoOtherUserReplies(loginUserId, replies);
        replyRepository.deleteByQuestionId(questionId);

        question.delete();
        questionRepository.update(question);
    }

    private void validNoOtherUserReplies(String loginUserId, List<Reply> replies) {
        boolean otherUserReplyExists = replies.stream().anyMatch(reply -> !reply.hasWriter(loginUserId));
        if (otherUserReplyExists) {
            throw new BadRequestException("다른 사용자의 댓글이 존재하는 게시글은 삭제할 수 없습니다.");
        }
    }

    public void validWriter(String loginUserId, Question question) {
        if (question.isValidWriter(loginUserId)) {
            throw new BadAuthenticationException("작성자만 수정, 삭제할 수 있습니다.");
        }
    }
}
