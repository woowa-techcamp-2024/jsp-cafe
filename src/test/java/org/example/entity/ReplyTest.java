package org.example.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.example.entity.Reply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReplyTest {

    @Test
    public void testReplyCreationWithId() {
        Reply reply = new Reply(1, "Test content", "author1", 101, false);

        assertEquals(1, reply.getReplyId());
        assertEquals("Test content", reply.getContent());
        assertEquals("author1", reply.getAuthorId());
        assertEquals(101, reply.getArticleId());
        assertFalse(reply.isDeleted());
    }

    @Test
    public void testReplyCreationWithoutId() {
        Reply reply = new Reply("Test content", "author1", 101);

        assertEquals("Test content", reply.getContent());
        assertEquals("author1", reply.getAuthorId());
        assertEquals(101, reply.getArticleId());
        assertFalse(reply.isDeleted());
    }

    @Test
    public void testSetReplyId() {
        Reply reply = new Reply("Test content", "author1", 101);
        reply.setReplyId(2);

        assertEquals(2, reply.getReplyId());
    }

    @Test
    public void testIsOwner() {
        Reply reply = new Reply("Test content", "author1", 101);

        assertTrue(reply.isOwner("author1"));
        assertFalse(reply.isOwner("author2"));
    }

    @Test
    public void testIsDeleted() {
        Reply deletedReply = new Reply(1, "Test content", "author1", 101, true);
        Reply nonDeletedReply = new Reply(2, "Test content", "author2", 102, false);

        assertTrue(deletedReply.isDeleted());
        assertFalse(nonDeletedReply.isDeleted());
    }
}
