package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Reply;
import com.woowa.cafe.utils.DBContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReplyRepositoryTest {

    ReplyRepository replyRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DBContainer.getDataSource();
        replyRepository = new JdbcReplyRepository(dataSource);

        String dropReplyTable = "DROP TABLE IF EXISTS replies";
        String createReplyTable = "CREATE TABLE replies (" +
                "reply_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "article_id BIGINT, " +
                "writer_id VARCHAR(255), " +
                "contents TEXT, " +
                "is_deleted BOOLEAN, " +
                "create_at TIMESTAMP, " +
                "modified_at TIMESTAMP, " +
                "index idx_reply_is_deleted (is_deleted))";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(dropReplyTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(createReplyTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void create() {
        // given
        Reply reply = new Reply(1L, "writer", "contents");

        // when
        replyRepository.save(reply);

        // then
        Optional<Reply> foundReply = replyRepository.findById(reply.getId());

        assertAll(
                () -> assertTrue(foundReply.isPresent()),
                () -> assertEquals(reply, foundReply.get())
        );
    }

    @Test
    @DisplayName("댓글을 수정할 수 있다.")
    void update() {
        // given
        Reply reply = new Reply(1L, "writer", "contents");
        replyRepository.save(reply);

        // when
        reply.update("updated contents");
        replyRepository.update(reply);

        // then
        Optional<Reply> foundReply = replyRepository.findById(reply.getId());

        assertAll(
                () -> assertTrue(foundReply.isPresent()),
                () -> assertEquals("updated contents", foundReply.get().getContents())
        );
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void delete() {
        // given
        Reply reply = new Reply(1L, "writer", "contents");
        replyRepository.save(reply);

        // when
        replyRepository.delete(reply.getId());

        // then
        Optional<Reply> foundReply = replyRepository.findById(reply.getId());

        assertFalse(foundReply.isPresent());
    }

    @Test
    @DisplayName("게시글에 해당하는 댓글을 조회할 수 있다.")
    void findByArticleId() {
        // given
        Reply reply1 = new Reply(1L, "writer", "contents");
        Reply reply2 = new Reply(1L, "writer", "contents");
        Reply reply3 = new Reply(2L, "writer", "contents");

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.save(reply3);

        // when
        Iterable<Reply> replies = replyRepository.findByArticleId(1L);

        // then
        assertEquals(2, replies.spliterator().getExactSizeIfKnown());
    }

    @Test
    @DisplayName("댓글을 조회할 수 있다.")
    void findById() {
        // given
        Reply reply = new Reply(1L, "writer", "contents");
        replyRepository.save(reply);

        // when
        Optional<Reply> foundReply = replyRepository.findById(reply.getId());

        // then
        assertAll(
                () -> assertTrue(foundReply.isPresent()),
                () -> assertEquals(reply, foundReply.get())
        );
    }

    @Test
    @DisplayName("모든 댓글을 조회할 수 있다.")
    void findAll() {
        // given
        Reply reply1 = new Reply(1L, "writer", "contents");
        Reply reply2 = new Reply(1L, "writer", "contents");
        Reply reply3 = new Reply(2L, "writer", "contents");

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.save(reply3);

        // when
        List<Reply> all = replyRepository.findAll();

        // then
        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("글의 댓글들을 삭제할 수 있다.")
    void deleteByArticleId() {
        // given
        Reply reply1 = new Reply(1L, "writer", "contents");
        Reply reply2 = new Reply(1L, "writer", "contents");
        Reply reply3 = new Reply(2L, "writer", "contents");

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.save(reply3);

        // when
        replyRepository.deleteByArticleId(1L);

        // then
        List<Reply> deleteReplies = replyRepository.findByArticleId(1L);
        List<Reply> saveReplies = replyRepository.findByArticleId(2L);
        assertAll(
                () -> assertEquals(0, deleteReplies.size()),
                () -> assertEquals(1, saveReplies.size())
        );
    }

}
