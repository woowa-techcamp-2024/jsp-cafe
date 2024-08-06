package com.woowa.database;

import com.woowa.model.Author;
import com.woowa.model.QuestionInfo;
import com.woowa.model.Reply;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReplyJdbcDatabase implements ReplyDatabase {
    @Override
    public void save(Reply reply) {
        String sql = "insert into reply (reply_id, content, user_id, question_id, created_at, deleted) values (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reply.getReplyId());
            pstmt.setString(2, reply.getContent());
            pstmt.setString(3, reply.getAuthor().getUserId());
            pstmt.setString(4, reply.getQuestionInfo().getQuestionId());
            pstmt.setTimestamp(5, Timestamp.valueOf(reply.getCreatedAt().toLocalDateTime()));
            pstmt.setBoolean(6, reply.isDeleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }

    @Override
    public List<Reply> findAll() {
        String sql = "select * from reply r join user u on r.user_id=u.user_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reply> replies = new ArrayList<>();
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                replies.add(mapToReply(rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Reply> findById(String replyId) {
        String sql = "select * from reply r join user u on r.user_id=u.user_id where r.reply_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, replyId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Reply reply = mapToReply(rs);
                return Optional.of(reply);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    private Reply mapToReply(ResultSet rs) throws SQLException {
        return Reply.create(
                rs.getString("reply_id"),
                rs.getString("content"),
                new Author(
                        rs.getString("user_id"),
                        rs.getString("nickname")
                ),
                new QuestionInfo(
                        rs.getString("question_id"),
                        ""
                ),
                ZonedDateTime.of(rs.getTimestamp("created_at").toLocalDateTime(), ZoneId.of("Asia/Seoul")),
                rs.getBoolean("deleted")
        );
    }

    @Override
    public void delete(Reply reply) {
        String sql = "update reply set deleted = true where reply_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reply.getReplyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }

    @Override
    public Page<Reply> findAllByQuestionId(String questionId, int page, int size) {
        List<Reply> replyPage = findAllByQuestionIdOrderByCreatedAt(questionId, page, size);
        Long totalElements = countRepliesByQuestionId(questionId);
        return Page.of(replyPage, totalElements, page, size);
    }

    private List<Reply> findAllByQuestionIdOrderByCreatedAt(String questionId, int page, int size) {
        String sql = "select * from reply r join user u on r.user_id=u.user_id"
                + " where r.question_id=?"
                + " order by created_at desc"
                + " limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, questionId);
            pstmt.setLong(2, page);
            pstmt.setLong(3, size);
            rs = pstmt.executeQuery();
            List<Reply> replies = new ArrayList<>();
            while (rs.next()) {
                replies.add(mapToReply(rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    private Long countRepliesByQuestionId(String questionId) {
        String sql = "select coalesce(*, 0) as reply_count from reply r where r.question_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, questionId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("reply_count");
            }
            return 0L;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }
}
