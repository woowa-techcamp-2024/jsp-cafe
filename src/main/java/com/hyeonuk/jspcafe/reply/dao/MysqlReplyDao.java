package com.hyeonuk.jspcafe.reply.dao;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.db.DBManager;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.reply.domain.Reply;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlReplyDao implements ReplyDao{
    private DBManager manager;
    public MysqlReplyDao(DBManager manager) {
        this.manager = manager;
    }

    @Override
    public Reply save(Reply reply) {
        try(Connection conn = manager.getConnection()) {
            if(reply.getId() == null){//save작업
                Long articleId = reply.getArticle().getId();
                Long memberId = reply.getMember().getId();
                String contents = reply.getContents();

                String query = "insert into reply(memberId,articleId,contents) values(?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setLong(1, memberId);
                pstmt.setLong(2, articleId);
                pstmt.setString(3, contents);
                int num = pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()){
                    reply.setId(rs.getLong(1));
                }
            }
            else{//update 작업
                Long id = reply.getId();
                String contents = reply.getContents();
                Optional<Reply> byId = findById(id);

                if(byId.isPresent()) {
                    String query = "update reply set contents = ? where id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1,contents);
                    pstmt.setLong(2, id);
                    pstmt.executeUpdate();
                }
                else{
                    Long memberId = reply.getMember().getId();
                    Long articleId = reply.getArticle().getId();
                    String query = "insert into reply(id,memberId,articleId,contents) values(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setLong(1,id);
                    pstmt.setLong(2,memberId);
                    pstmt.setLong(3,articleId);
                    pstmt.setString(4,contents);

                    pstmt.executeUpdate();
                }
            }
            return reply;
        }catch(SQLException e) {
            throw new HttpInternalServerErrorException("error");
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection conn = manager.getConnection()){
            String sql = "update reply set deletedAt = now() where id = ? and deletedAt is null";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public void deleteAllByArticleId(Long articleId) {
        try(Connection conn = manager.getConnection()){
            String sql = "update reply set deletedAt = now() where articleId = ? and deletedAt is null";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public List<Reply> findAllByArticleId(Long articleId) {
        try(Connection conn = manager.getConnection()){
            String sql = "select r.id as \"r.id\",r.articleId as \"r.articleId\",r.contents as \"r.contents\",  " +
                    "m.id as \"m.id\", m.nickname as \"m.nickname\", m.email as \"m.email\", m.memberId as \"m.memberId\" " +
                    "from reply r left join member m on r.memberId = m.id where r.articleId = ? and r.deletedAt is null";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, articleId);
            ResultSet rs = pstmt.executeQuery();
            List<Reply> replies = new ArrayList<>();
            while(rs.next()){
                replies.add(mappingReply(rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public List<Reply> findAllByArticleId(Long articleId, int size, int page) {
        try(Connection conn = manager.getConnection()){
            String sql = "select r.id as \"r.id\",r.articleId as \"r.articleId\",r.contents as \"r.contents\",  " +
                    "m.id as \"m.id\", m.nickname as \"m.nickname\", m.email as \"m.email\", m.memberId as \"m.memberId\" " +
                    "from reply r left join member m on r.memberId = m.id where r.articleId = ? and r.deletedAt is null " +
                    "LIMIT ? OFFSET ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, articleId);
            pstmt.setInt(2,size);
            pstmt.setInt(3,(page-1)*size);
            ResultSet rs = pstmt.executeQuery();
            List<Reply> replies = new ArrayList<>();
            while(rs.next()){
                replies.add(mappingReply(rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public long count() {
        try(Connection conn = manager.getConnection()){
            String sql = "select count(*) from reply";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }catch(SQLException e){
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public long countByArticleId(Long articleId) {
        try(Connection conn = manager.getConnection()){
            String sql = "select count(*) from reply where articleId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,articleId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }catch(SQLException e){
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public Optional<Reply> findById(Long id) {
        try(Connection conn = manager.getConnection()){
            String sql = "select r.id as \"r.id\",r.articleId as \"r.articleId\",r.contents as \"r.contents\"," +
                    "m.id as \"m.id\", m.nickname as \"m.nickname\", m.email as \"m.email\", m.memberId as \"m.memberId\" " +
                    "from reply r left join member m on m.id = r.memberId where r.id = ? and r.deletedAt is null";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            Reply reply = null;
            if(rs.next()) {
                reply = mappingReply(rs);
            }
            return Optional.ofNullable(reply);
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    private Reply mappingReply(ResultSet rs) throws SQLException {
        Long replyId = rs.getLong("r.id");
        Long articleId = rs.getLong("r.articleId");
        String contents = rs.getString("r.contents");
        Long mId = rs.getLong("m.id");
        String nickname = rs.getString("m.nickname");
        String email = rs.getString("m.email");
        String memberId = rs.getString("m.memberId");
        Member member = new Member(mId,memberId,"",nickname,email);
        Reply reply = new Reply(replyId,new Article(articleId,null,"",""),member,contents);
        return reply;
    }
}
