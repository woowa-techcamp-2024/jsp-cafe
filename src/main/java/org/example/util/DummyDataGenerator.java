package org.example.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class DummyDataGenerator {

    private static final DatabaseConnectionPool connectionPool = new DatabaseConnectionPool();

    private static final int BATCH_SIZE = 1000;
    private static final int USER_COUNT = 50000;
    private static final int POST_COUNT = 1000000;
    private static final Random random = new Random();

    public static void main(String[] args) {
        try (Connection conn = connectionPool.getConnection()) {
            conn.setAutoCommit(false);

            insertUsers(conn);
            insertPosts(conn);
            insertReplies(conn);

            conn.commit();
            System.out.println("데이터 삽입 완료!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUsers(Connection conn) throws SQLException {
        String sql = "INSERT INTO users (user_id, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < USER_COUNT; i++) {
                pstmt.setString(1, "user" + i);
                pstmt.setString(2, "Name" + i);
                pstmt.setString(3, "user" + i + "@example.com");
                pstmt.setString(4, UUID.randomUUID().toString());
                pstmt.addBatch();

                if (i % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // 남은 배치 실행
        }
        System.out.println(USER_COUNT + "명의 사용자 삽입 완료");
    }

    private static void insertPosts(Connection conn) throws SQLException {
        String sql = "INSERT INTO posts (user_id, title, contents, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < POST_COUNT; i++) {
                pstmt.setString(1, "user" + random.nextInt(USER_COUNT));
                pstmt.setString(2, "Title " + i);
                pstmt.setString(3, "Contents of post " + i);
                pstmt.setString(4, "AVAILABLE");
                pstmt.addBatch();

                if (i % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // 남은 배치 실행
        }
        System.out.println(POST_COUNT + "개의 게시글 삽입 완료");
    }

    private static void insertReplies(Connection conn) throws SQLException {
        String sql = "INSERT INTO replies (post_id, user_id, contents, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= POST_COUNT; i++) {
                int replyCount = random.nextInt(11); // 0-10 개의 댓글
                for (int j = 0; j < replyCount; j++) {
                    pstmt.setLong(1, i);
                    pstmt.setString(2, "user" + random.nextInt(USER_COUNT));
                    pstmt.setString(3, "Reply " + j + " to post " + i);
                    pstmt.setString(4, "AVAILABLE");
                    pstmt.addBatch();
                }

                if (i % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // 남은 배치 실행
        }
        System.out.println("댓글 삽입 완료");
    }
}