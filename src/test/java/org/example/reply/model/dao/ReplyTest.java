package org.example.reply.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.example.reply.model.ReplyStatus;
import org.junit.jupiter.api.Test;

public class ReplyTest {

    @Test
    public void test_create_reply_with_valid_fields() {
        // Given
        String userId = "user123";
        Long postId = 1L;
        String contents = "This is a reply";

        // When
        Reply reply = Reply.create(userId, postId, contents);

        // Then
        assertNotNull(reply);
        assertEquals(userId, reply.getUserId());
        assertEquals(postId, reply.getPostId());
        assertEquals(contents, reply.getContents());
        assertEquals(ReplyStatus.AVAILABLE, reply.getReplyStatus());
        assertNotNull(reply.getCreatedAt());
    }

    // Creating a reply with all fields including id, postId, userId, contents, replyStatus, and createdAt
    @Test
    public void test_create_reply_with_all_fields() {
        // Given
        Long id = 1L;
        Long postId = 1L;
        String userId = "user123";
        String contents = "This is a reply";
        ReplyStatus replyStatus = ReplyStatus.AVAILABLE;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        Reply reply = Reply.createWithAll(id, postId, userId, contents, replyStatus, createdAt);

        // Then
        assertNotNull(reply);
        assertEquals(id, reply.getId());
        assertEquals(postId, reply.getPostId());
        assertEquals(userId, reply.getUserId());
        assertEquals(contents, reply.getContents());
        assertEquals(replyStatus, reply.getReplyStatus());
        assertEquals(createdAt, reply.getCreatedAt());
    }

    // Retrieving the id of a reply
    @Test
    public void test_get_id_of_reply() {
        // Given
        Long id = 1L;
        Reply reply = Reply.createWithAll(id, 1L, "user123", "This is a reply", ReplyStatus.AVAILABLE,
                LocalDateTime.now());

        // When
        Long retrievedId = reply.getId();

        // Then
        assertEquals(id, retrievedId);
    }

    // Retrieving the postId of a reply
    @Test
    public void test_get_post_id_of_reply() {
        // Given
        Long postId = 1L;
        Reply reply = Reply.createWithAll(1L, postId, "user123", "This is a reply", ReplyStatus.AVAILABLE,
                LocalDateTime.now());

        // When
        Long retrievedPostId = reply.getPostId();

        // Then
        assertEquals(postId, retrievedPostId);
    }

    // Retrieving the userId of a reply
    @Test
    public void test_get_user_id_of_reply() {
        // Given
        String userId = "user123";
        Reply reply = Reply.createWithAll(1L, 1L, userId, "This is a reply", ReplyStatus.AVAILABLE,
                LocalDateTime.now());

        // When
        String retrievedUserId = reply.getUserId();

        // Then
        assertEquals(userId, retrievedUserId);
    }

    // Creating a reply with null postId
    @Test
    public void test_create_reply_with_null_post_id() {
        // Given
        String userId = "user123";
        String contents = "This is a reply";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(userId, null, contents);
        });
    }

    // Creating a reply with null or empty contents
    @Test
    public void test_create_reply_with_null_or_empty_contents() {
        // Given
        String userId = "user123";
        Long postId = 1L;

        // When & Then (null contents)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(userId, postId, null);
        });

        // When & Then (empty contents)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(userId, postId, "");
        });

        // When & Then (whitespace contents)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(userId, postId, "   ");
        });
    }

    // Creating a reply with null or empty userId
    @Test
    public void test_create_reply_with_null_or_empty_user_id() {
        // Given
        Long postId = 1L;
        String contents = "This is a reply";

        // When & Then (null userId)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(null, postId, contents);
        });

        // When & Then (empty userId)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create("", postId, contents);
        });

        // When & Then (whitespace userId)
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create("   ", postId, contents);
        });
    }

    // Creating a reply with null replyStatus
    @Test
    public void test_create_reply_with_null_reply_status() {
        // Given
        Long id = 1L;
        Long postId = 1L;
        String userId = "user123";
        String contents = "This is a reply";
        LocalDateTime createdAt = LocalDateTime.now();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.createWithAll(id, postId, userId, contents, null, createdAt);
        });
    }

    // Creating a reply with null createdAt
    @Test
    public void test_create_reply_with_null_created_at() {
        // Given
        Long id = 1L;
        Long postId = 1L;
        String userId = "user123";
        String contents = "This is a reply";
        ReplyStatus replyStatus = ReplyStatus.AVAILABLE;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.createWithAll(id, postId, userId, contents, replyStatus, null);
        });
    }

}