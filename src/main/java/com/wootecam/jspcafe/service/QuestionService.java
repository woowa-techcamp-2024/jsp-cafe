package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.domain.ReplyRepository;
import com.wootecam.jspcafe.exception.BadRequestException;
import com.wootecam.jspcafe.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    public QuestionService(final QuestionRepository questionRepository, final ReplyRepository replyRepository) {
        this.questionRepository = questionRepository;
        this.replyRepository = replyRepository;
    }

    public void append(final String writer, final String title, final String contents, final Long writerId) {
        Question question = new Question(writer, title, contents, LocalDateTime.now(), writerId);

        log.info("write question = {}", question);

        questionRepository.save(question);
    }

    public int countAll() {
        return questionRepository.countAll();
    }

    public List<Question> readAll(final int page, final int size) {
        return questionRepository.findAllOrderByCreatedTimeDesc(page, size);
    }

    public Question read(final Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("질문을 찾을 수 없습니다. id = " + id));
    }

    public Question readQuestionToEdit(final Long questionId, final Long signInUserId) {
        if (Objects.isNull(questionId)) {
            throw new NotFoundException("수정 할 질문을 찾을 수 없습니다.");
        }

        Question question = read(questionId);

        if (!question.isSameWriter(signInUserId)) {
            throw new BadRequestException("다른 사용자의 질문은 수정할 수 없습니다.");
        }

        return question;
    }

    public void edit(final Long questionId, final String editedTitle, final String editedContents) {
        if (Objects.isNull(editedTitle) || Objects.isNull(editedContents)
                || editedTitle.isEmpty() || editedContents.isEmpty()) {
            throw new BadRequestException("질문 수정 시 제목과 내용을 모두 입력해야 합니다.");
        }

        questionRepository.update(questionId, editedTitle, editedContents);
    }

    public void delete(final Long questionId, final Long signInUserId) {
        if (Objects.isNull(questionId)) {
            throw new NotFoundException("삭제 할 질문을 찾을 수 없습니다.");
        }
        Question question = read(questionId);

        if (!question.isSameWriter(signInUserId)) {
            throw new BadRequestException("다른 사용자의 질문은 삭제할 수 없습니다.");
        }

        if (replyRepository.existsReplyByIdAndOtherUserPrimaryId(questionId, signInUserId)) {
            throw new BadRequestException("다른 사용자가 댓글을 단 질문은 삭제할 수 없습니다.");
        }

        replyRepository.deleteAllByQuestionPrimaryId(questionId);
        questionRepository.deleteById(questionId);
    }
}
