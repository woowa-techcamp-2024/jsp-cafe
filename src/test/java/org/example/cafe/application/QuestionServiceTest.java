package org.example.cafe.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.example.cafe.common.exception.BadRequestException;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("QuestionService 단위 테스트")
class QuestionServiceTest {

    private QuestionRepository questionRepository = mock(QuestionRepository.class);
    private ReplyRepository replyRepository = mock(ReplyRepository.class);
    private QuestionService questionService = new QuestionService(questionRepository, replyRepository);

    @Nested
    class 게시글을_삭제한다 {

        @Test
        void 게시글을_삭제할_수_있다() {
            String loginUserId = "user1";
            Long questionId = 1L;
            Question question = new Question(questionId, "title", "content", loginUserId);
            List<Reply> replies = Collections.emptyList();

            when(questionRepository.findById(questionId)).thenReturn(question);
            when(replyRepository.findByQuestionId(questionId)).thenReturn(replies);

            questionService.deleteQuestion(loginUserId, questionId);

            verify(questionRepository).update(question);
            assertTrue(question.isDeleted());
        }

        @Test
        void 게시글_작성자가_쓴_댓글만_있다면_게시글을_삭제할_수_있다() {
            String loginUserId = "user1";
            Long questionId = 1L;
            Question question = new Question(questionId, "title", "content", loginUserId);
            Reply reply1 = new Reply.ReplyBuilder().replyId(1L).writer(loginUserId).questionId(questionId).build();
            Reply reply2 = new Reply.ReplyBuilder().replyId(2L).writer(loginUserId).questionId(questionId).build();
            List<Reply> replies = List.of(reply1, reply2);

            when(questionRepository.findById(questionId)).thenReturn(question);
            when(replyRepository.findByQuestionId(questionId)).thenReturn(replies);

            questionService.deleteQuestion(loginUserId, questionId);

            verify(replyRepository).update(reply1);
            verify(replyRepository).update(reply2);
        }

        @Test
        void 다른_사용자의_댓글이_있다면_예외가_발생한다() {
            String loginUserId = "user1";
            Long questionId = 1L;
            Question question = new Question(questionId, "title", "content", loginUserId);
            Reply reply1 = new Reply.ReplyBuilder().replyId(1L).writer("user2").questionId(questionId).build();
            List<Reply> replies = List.of(reply1);

            when(questionRepository.findById(questionId)).thenReturn(question);
            when(replyRepository.findByQuestionId(questionId)).thenReturn(replies);

            assertThrows(BadRequestException.class, () -> {
                questionService.deleteQuestion(loginUserId, questionId);
            });
        }
    }
}