package com.woowa.database;

import com.mysql.cj.protocol.Resultset;
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

public class ReplyJdbcDatabase implements ReplyDatabase {
    @Override
    public void save(Reply reply) {
        String sql = "insert into reply (reply_id, content, user_id, question_id, created_at) values (?, ?, ?, ?, ?)";

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
                replies.add(Reply.create(
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
                        ZonedDateTime.of(rs.getTimestamp("created_at").toLocalDateTime(), ZoneId.of("Asia/Seoul"))
                ));
            }
            return replies;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외 발생", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }
}
