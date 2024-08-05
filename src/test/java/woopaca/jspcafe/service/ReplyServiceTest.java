package woopaca.jspcafe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import woopaca.jspcafe.error.BadRequestException;
import woopaca.jspcafe.error.NotFoundException;
import woopaca.jspcafe.mock.MockPostRepository;
import woopaca.jspcafe.mock.MockReplyRepository;
import woopaca.jspcafe.mock.MockUserRepository;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.Reply;
import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.ReplyRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.request.WriteReplyRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplyServiceTest {

    private ReplyRepository replyRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private ReplyService replyService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        replyRepository = new MockReplyRepository();
        postRepository = new MockPostRepository();
        userRepository = new MockUserRepository();
        replyService = new ReplyService(replyRepository, postRepository, userRepository);

        user = new User("test", "test", "test");
        userRepository.save(user);
        post = new Post("title", "content", user.getId());
        postRepository.save(post);
    }

    @Nested
    class writeReply_메서드는 {

        @Nested
        class 유효한_댓글_작성_요청 {

            @Test
            void 정상적으로_댓글을_작성한다() {
                Authentication authentication = new Authentication(user, LocalDateTime.now());

                WriteReplyRequest writeReplyRequest = new WriteReplyRequest(post.getId(), "comment");
                replyService.writeReply(writeReplyRequest, authentication);
                replyService.writeReply(writeReplyRequest, authentication);

                assertThat(replyRepository.findByPostId(post.getId())).hasSize(2);
            }
        }

        @Nested
        class 유효하지_않은_댓글_작성_요청 {

            @Test
            void 댓글_내용이_비어있으면_예외가_발생한다() {
                Authentication authentication = new Authentication(user, LocalDateTime.now());

                WriteReplyRequest writeReplyRequest = new WriteReplyRequest(post.getId(), "");
                assertThatThrownBy(() -> replyService.writeReply(writeReplyRequest, authentication))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("[ERROR] 댓글 내용을 입력해주세요.");
            }

            @Test
            void 댓글_내용이_200자를_초과하면_예외가_발생한다() {
                Authentication authentication = new Authentication(user, LocalDateTime.now());

                WriteReplyRequest writeReplyRequest = new WriteReplyRequest(post.getId(), "a".repeat(201));
                assertThatThrownBy(() -> replyService.writeReply(writeReplyRequest, authentication))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("[ERROR] 댓글 내용은 200자 이하여야 합니다.");
            }

            @Test
            void 존재하지_않는_게시글에_댓글을_작성하려고_하면_예외가_발생한다() {
                Authentication authentication = new Authentication(user, LocalDateTime.now());

                WriteReplyRequest writeReplyRequest = new WriteReplyRequest(-1L, "comment");
                assertThatThrownBy(() -> replyService.writeReply(writeReplyRequest, authentication))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessage("[ERROR] 게시글을 찾을 수 없습니다.");
            }

            @Test
            void 삭제된_게시글에_댓글을_작성하려고_하면_예외가_발생한다() {
                post.softDelete();
                postRepository.save(post);
                Authentication authentication = new Authentication(user, LocalDateTime.now());

                WriteReplyRequest writeReplyRequest = new WriteReplyRequest(post.getId(), "comment");
                assertThatThrownBy(() -> replyService.writeReply(writeReplyRequest, authentication))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("[ERROR] 삭제된 게시글에는 댓글을 작성할 수 없습니다.");
            }
        }
    }

    @Nested
    class getReplies_메서드는 {

        @Test
        void 게시글에_작성된_댓글을_조회한다() {
            replyRepository.save(new Reply("comment1", user.getId(), post.getId()));
            replyRepository.save(new Reply("comment2", user.getId(), post.getId()));

            assertThat(replyService.getReplies(post.getId())).hasSize(2);
        }

        @Test
        void 삭제된_댓글은_조회하지_않는다() {
            Reply reply = new Reply("comment1", user.getId(), post.getId());
            replyRepository.save(reply);
            reply.softDelete();
            replyRepository.save(reply);

            assertThat(replyService.getReplies(post.getId())).isEmpty();
        }

        @Test
        void 존재하지_않는_게시글의_댓글을_조회하려고_하면_예외가_발생한다() {
            assertThatThrownBy(() -> replyService.getReplies(-1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("[ERROR] 게시글을 찾을 수 없습니다.");
        }

        @Test
        void 삭제된_게시글의_댓글을_조회하려고_하면_예외가_발생한다() {
            post.softDelete();
            postRepository.save(post);

            assertThatThrownBy(() -> replyService.getReplies(post.getId()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("[ERROR] 삭제된 게시글입니다.");
        }
    }

    @Nested
    class deleteReply_메서드는 {

        @Test
        void 댓글을_삭제한다() {
            Reply reply = new Reply("comment", user.getId(), post.getId());
            replyRepository.save(reply);
            Authentication authentication = new Authentication(user, LocalDateTime.now());

            replyService.deleteReply(reply.getId(), authentication);

            assertThat(reply.isPublished()).isFalse();
        }

        @Test
        void 다른_사용자의_댓글을_삭제하려고_하면_예외가_발생한다() {
            User anotherUser = new User("another", "another", "another");
            userRepository.save(anotherUser);
            Reply reply = new Reply("comment", anotherUser.getId(), post.getId());
            replyRepository.save(reply);
            Authentication authentication = new Authentication(user, LocalDateTime.now());

            assertThatThrownBy(() -> replyService.deleteReply(reply.getId(), authentication))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("[ERROR] 댓글 작성자만 삭제할 수 있습니다.");
        }

        @Test
        void 존재하지_않는_댓글을_삭제하려고_하면_예외가_발생한다() {
            Authentication authentication = new Authentication(user, LocalDateTime.now());

            assertThatThrownBy(() -> replyService.deleteReply(-1L, authentication))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("[ERROR] 댓글을 찾을 수 없습니다.");
        }
    }
}
