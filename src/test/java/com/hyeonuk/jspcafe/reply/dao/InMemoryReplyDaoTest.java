package com.hyeonuk.jspcafe.reply.dao;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.reply.domain.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryReplyDao 클래스")
class InMemoryReplyDaoTest {
    private MemberDao memberDao = new InMemoryMemberDao();
    private ArticleDao articleDao = new InMemoryArticleDao();
    private ReplyDao replyDao = new InMemoryReplyDao();

    private Member member = new Member(1l,"memberId1","password1","nickname1","email1");
    private Member member2 = new Member(2l,"memberId2","password2","nickname2","email2");
    private Article article = new Article(1l,member,"title1","contents1");

    @BeforeEach
    void setUp(){
        memberDao.save(member);
        memberDao.save(member2);
        articleDao.save(article);
    }

    @Nested
    @DisplayName("save 메서드는")
    class SaveMethod{
        @Test
        @DisplayName("댓글의 ID값이 없으면 저장된다.")
        void saveWithoutId(){
            //given
            Reply reply = new Reply(article,member2,"reply1");

            //when
            replyDao.save(reply);

            //then
            Optional<Reply> byId = replyDao.findById(reply.getId());
            assertTrue(byId.isPresent());
            Reply saved = byId.get();
            assertEquals(reply.getId(),saved.getId());
            assertEquals(reply.getArticle().getId(),saved.getArticle().getId());
            assertEquals(reply.getMember().getId(),saved.getMember().getId());
        }

        @Test
        @DisplayName("댓글의 ID값이 있으나, db에 정보가 없으면 저장된다..")
        void saveWithId(){
            //given
            Reply reply = new Reply(1l,article,member2,"reply1");

            //when
            replyDao.save(reply);

            //then
            Optional<Reply> byId = replyDao.findById(reply.getId());
            assertTrue(byId.isPresent());
            Reply saved = byId.get();
            assertEquals(reply.getId(),saved.getId());
            assertEquals(reply.getArticle().getId(),saved.getArticle().getId());
            assertEquals(reply.getMember().getId(),saved.getMember().getId());
        }

        @Test
        @DisplayName("댓글의 ID값이 있으나, db에 정보가 있으면 업데이트 된다..")
        void saveWithIdUpdate(){
            //given
            Reply reply = new Reply(1l,article,member2,"reply1");
            replyDao.save(reply);
            Reply update = new Reply(1l,article,member2,"reply2");

            //when
            replyDao.save(update);

            //then
            Optional<Reply> byId = replyDao.findById(update.getId());
            assertTrue(byId.isPresent());
            Reply saved = byId.get();
            assertEquals(update.getId(),saved.getId());
            assertEquals(update.getArticle().getId(),saved.getArticle().getId());
            assertEquals(update.getMember().getId(),saved.getMember().getId());
            assertEquals(1,replyDao.findAllByArticleId(article.getId()).size());
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteByIdMethod{
        @Test
        @DisplayName("저장된 id를 입력받으면 잘 삭제된다.")
        void deleteById(){
            //given
            Reply reply = new Reply(1l,article,member2,"reply1");
            replyDao.save(reply);

            //when
            replyDao.deleteById(reply.getId());

            //then
            assertTrue(replyDao.findById(reply.getId()).isEmpty());
        }
        @Test
        @DisplayName("저장된 id가 없으면 아무일도 안생긴다.")
        void deleteByIdWithoutData(){
            //given
            Long replyId = Long.MAX_VALUE;
            boolean isEmpty = replyDao.findById(replyId).isEmpty();

            //when
            replyDao.deleteById(replyId);

            //then
            assertTrue(isEmpty);
            assertTrue(replyDao.findById(replyId).isEmpty());
        }
    }

    @Nested
    @DisplayName("deleteAllByArticleId 메서드는")
    class DeleteAllByArticleIdMethod{
        @Test
        @DisplayName("articleId에 해당하는 reply를 모두 삭제한다. 다른 게시글의 댓글은 삭제되지 않는다.")
        void deleteAllByArticleId(){
            //given
            Article otherArticle = new Article(member,"other title","other contents");
            articleDao.save(otherArticle);
            Reply otherReply = new Reply(otherArticle,member,"contents");
            replyDao.save(otherReply);

            Reply reply1 = new Reply(article,member2,"reply1");
            Reply reply2 = new Reply(article,member2,"reply2");
            Reply reply3 = new Reply(article,member,"reply3");
            replyDao.save(reply1);
            replyDao.save(reply2);
            replyDao.save(reply3);

            //when
            replyDao.deleteAllByArticleId(article.getId());

            //then
            assertTrue(replyDao.findById(reply1.getId()).isEmpty());
            assertTrue(replyDao.findById(reply2.getId()).isEmpty());
            assertTrue(replyDao.findById(reply3.getId()).isEmpty());
            assertTrue(replyDao.findAllByArticleId(article.getId()).isEmpty());
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindByIdMethod{
        @Test
        @DisplayName("id값이 존재하면 해당 reply를 반환한다.")
        void findById(){
            //given
            Reply reply = new Reply(1l,article,member,"reply");
            replyDao.save(reply);

            //when
            Optional<Reply> byId = replyDao.findById(reply.getId());

            //then
            assertTrue(byId.isPresent());
            Reply find = byId.get();
            assertEquals(reply.getId(),find.getId());
            assertEquals(reply.getContents(),find.getContents());
            assertEquals(reply.getMember().getId(),find.getMember().getId());
            assertEquals(reply.getArticle().getId(),find.getArticle().getId());
        }

        @Test
        @DisplayName("삭제된 id값은 null을 반환한다.")
        void findByWithDeletedId(){
            //given
            Reply reply = new Reply(1l,article,member,"reply");
            replyDao.save(reply);
            replyDao.deleteById(reply.getId());

            //when
            Optional<Reply> byId = replyDao.findById(reply.getId());

            //then
            assertTrue(byId.isEmpty());
        }

        @Test
        @DisplayName("존재하지 않는 값은 null을 반환한다.")
        void findByNoneId(){
            //given

            //when
            Optional<Reply> byId = replyDao.findById(Long.MAX_VALUE);

            //then
            assertTrue(byId.isEmpty());
        }
    }

    @Nested
    @DisplayName("findAllByArticleId 메서드는")
    class FindAllByArticleIdMethod{
        @Test
        @DisplayName("articleId에 해당하는 댓글을 모두 가져온다.")
        void findAllByArticleId(){
            //given
            Reply reply1 = new Reply(1l,article,member,"reply1");
            Reply reply2 = new Reply(2l,article,member,"reply2");
            Reply reply3 = new Reply(3l,article,member,"reply3");
            List<Reply> replies = List.of(reply1,reply2,reply3);
            replyDao.save(reply1);
            replyDao.save(reply2);
            replyDao.save(reply3);

            //when
            List<Reply> findAll = replyDao.findAllByArticleId(article.getId());

            //then
            assertEquals(replies.size(),findAll.size());
        }

        @Test
        @DisplayName("articleId에 해당하는 댓글이 존재하지 않는다면 빈 리스트를 반환한다..")
        void findAllByArticleIdWithEmptyList(){
            //given

            //when
            List<Reply> findAll = replyDao.findAllByArticleId(article.getId());

            //then
            assertTrue(findAll.isEmpty());
        }
    }
}