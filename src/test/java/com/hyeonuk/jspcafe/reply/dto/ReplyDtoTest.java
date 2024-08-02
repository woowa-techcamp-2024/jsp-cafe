package com.hyeonuk.jspcafe.reply.dto;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.reply.domain.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReplyDto 테스트")
public class ReplyDtoTest {

    @Nested
    @DisplayName("from 메서드")
    class FromMethod {
        @Test
        @DisplayName("Reply를 ReplyDto로 변환")
        void fromReplyToReplyDto() {
            // Given
            Long replyId = 1L;
            Long memberId = 2L;
            Long articleId = 3L;
            String contents = "This is a reply.";
            String nickname = "JohnDoe";

            Member member = new Member(memberId, "john", "password", nickname, "john@example.com");
            Article article = new Article(articleId, member, "Article Content", "Author");
            Reply reply = new Reply(replyId, article, member, contents);

            // When
            ReplyDto replyDto = ReplyDto.from(reply);

            // Then
            assertEquals(replyId, replyDto.getReplyId());
            assertEquals(memberId, replyDto.getMemberId());
            assertEquals(nickname, replyDto.getMemberNickname());
            assertEquals(articleId, replyDto.getArticleId());
            assertEquals(contents, replyDto.getContents());
        }
    }

    @Nested
    @DisplayName("Getter 및 Setter")
    class GettersAndSetters {
        @Test
        @DisplayName("필드 값 설정 및 가져오기")
        void testGettersAndSetters() {
            // Given
            Long replyId = 1L;
            Long memberId = 2L;
            Long articleId = 3L;
            String contents = "This is a reply.";
            String nickname = "JohnDoe";

            // When
            ReplyDto replyDto = new ReplyDto();
            replyDto.setReplyId(replyId);
            replyDto.setMemberId(memberId);
            replyDto.setArticleId(articleId);
            replyDto.setContents(contents);
            replyDto.setMemberNickname(nickname);

            // Then
            assertEquals(replyId, replyDto.getReplyId());
            assertEquals(memberId, replyDto.getMemberId());
            assertEquals(nickname, replyDto.getMemberNickname());
            assertEquals(articleId, replyDto.getArticleId());
            assertEquals(contents, replyDto.getContents());
        }
    }
}
