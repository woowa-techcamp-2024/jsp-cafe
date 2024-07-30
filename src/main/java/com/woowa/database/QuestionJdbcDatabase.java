package com.woowa.database;

import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.QuestionInfo;
import com.woowa.model.Reply;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionJdbcDatabase implements QuestionDatabase {
    @Override
    public void save(Question question) {
        String sql = "insert into question (question_id, title, content, user_id, created_at, deleted) values (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, question.getQuestionId());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContent());
            pstmt.setString(4, question.getAuthor().getUserId());
            pstmt.setTimestamp(5, Timestamp.valueOf(question.getCreatedAt().toLocalDateTime()));
            pstmt.setBoolean(6, question.isDeleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }

    private Question mapToQuestion(ResultSet rs) throws SQLException {
        return Question.create(
                rs.getString("question_id"),
                rs.getString("title"),
                rs.getString("content"),
                new Author(
                        rs.getString("user_id"),
                        rs.getString("nickname")
                ),
                ZonedDateTime.of(rs.getTimestamp("created_at").toLocalDateTime(),
                        ZoneId.of("Asia/Seoul")),
                rs.getBoolean("deleted")
        );
    }

    @Override
    public List<Question> findAll() {
        String sql = "select * from question q join user u on u.user_id = q.user_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Question> questions = new ArrayList<>();
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = mapToQuestion(rs);
                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public List<Question> findAllOrderByCreatedAt(int page, int size) {
        String sql = "select * from question q"
                + " join user u on u.user_id = q.user_id"
                + " where deleted is false"
                + " order by created_at desc"
                + " limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Question> questions = new ArrayList<>();
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, size);
            pstmt.setInt(2, size * page);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = mapToQuestion(rs);
                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Question> findById(String questionId) {
        String sql = "select * from question q join user u on q.user_id = u.user_id where question_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, questionId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToQuestion(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public void update(Question question) {
        String sql = "update question set title = ?, content = ? where question_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getContent());
            pstmt.setString(3, question.getQuestionId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }

    @Override
    public void delete(Question question) {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            con = DBConnectionUtils.getConnection();
            DBConnectionUtils.startTransaction(con);

            String sql = "update question set deleted = true where question_id = ?";
            pstmt1 = con.prepareStatement(sql);
            pstmt1.setString(1, question.getQuestionId());
            pstmt1.executeUpdate();

            sql = "update reply set deleted = true where question_id = ?";
            pstmt2 = con.prepareStatement(sql);
            pstmt2.setString(1, question.getQuestionId());
            pstmt2.executeUpdate();
            DBConnectionUtils.commit(con);
        } catch (SQLException e) {
            DBConnectionUtils.rollback(con);
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(null, pstmt2, null);
            DBConnectionUtils.closeConnection(con, pstmt1, null);
        }
    }

    @Override
    public Optional<Question> findByIdWithReplies(String questionId) {
        Optional<Question> optionalQuestion = findById(questionId);
        List<Reply> replies = findRepliesByQuestionId(questionId);
        optionalQuestion.ifPresent(q -> q.getReplies().addAll(replies));
        return optionalQuestion;
    }

    private List<Reply> findRepliesByQuestionId(String questionId) {
        String sql = "select * from reply r join user u on r.user_id = u.user_id where question_id = ? and deleted is false";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reply> replies = new ArrayList<>();
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, questionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                replies.add(mapToReply(questionId, rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Question> findByIdWithRepliesContainsDeleted(String questionId) {
        Optional<Question> optionalQuestion = findById(questionId);
        optionalQuestion.ifPresent(
                question -> question.getReplies()
                        .addAll(findRepliesContainsDeletedByQuestionId(questionId)));
        return optionalQuestion;
    }

    private List<Reply> findRepliesContainsDeletedByQuestionId(String questionId) {
        String sql = "select * from reply r join user u on r.user_id = u.user_id where question_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reply> replies = new ArrayList<>();
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, questionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                replies.add(mapToReply(questionId, rs));
            }
            return replies;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    private Reply mapToReply(String questionId, ResultSet rs) throws SQLException {
        return Reply.create(
                rs.getString("reply_id"),
                rs.getString("content"),
                new Author(
                        rs.getString("user_id"),
                        rs.getString("nickname")
                ),
                new QuestionInfo(
                        questionId,
                        ""
                ),
                ZonedDateTime.of(rs.getTimestamp("created_at").toLocalDateTime(), ZoneId.of("Asia/Seoul")),
                rs.getBoolean("deleted")
        );
    }
}
