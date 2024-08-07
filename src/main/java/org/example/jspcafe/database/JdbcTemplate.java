package org.example.jspcafe.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class JdbcTemplate {
    private static final int BATCH_SIZE = 1000;
    private static final int THREAD_COUNT = 4; // 사용할 쓰레드 수
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private static String URL;
    private static String MYSQL_USERNAME;
    private static String MYSQL_PASSWORD;
    private static Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, MYSQL_USERNAME, MYSQL_PASSWORD);
    }

    public static void initializeDatabase() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String mysqlUrl = System.getenv("MYSQL_URL");
        URL = mysqlUrl != null ? mysqlUrl : "jdbc:mysql://host.docker.internal:3306/jsp-cafe?allowMultiQueries=true";

        String mysqlUsername = System.getenv("MYSQL_USERNAME");
        MYSQL_USERNAME = mysqlUsername != null ? mysqlUsername : "root";

        String mysqlPassword = System.getenv("MYSQL_PASSWORD");
        MYSQL_PASSWORD = mysqlPassword != null ? mysqlPassword : "";


        try (Connection conn = DriverManager.getConnection(URL, MYSQL_USERNAME, MYSQL_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = readSqlFile("/init.sql");
            stmt.execute(sql);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        if ("true".equals(System.getenv("BATCH_INSERT"))) {
            try {
                insertUsers();
                insertQuestions();
                insertReplies();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("대용량 데이터 삽입 과정에서 에러 발생");
            } finally {
                shutdownExecutorService();
            }
        }
    }


    private static void insertUsers() throws InterruptedException {
        String sql = "INSERT INTO Users (user_id, password, nickname, email) VALUES (?, ?, ?, ?)";
        int totalUsers = 10000;
        int usersPerThread = totalUsers / THREAD_COUNT;
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            executorService.submit(() -> {
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    conn.setAutoCommit(false);
                    for (int i = 1 + threadId * usersPerThread; i <= (threadId + 1) * usersPerThread; i++) {
                        pstmt.setString(1, "test" + i);
                        pstmt.setString(2, "test");
                        pstmt.setString(3, "닉네임" + i);
                        pstmt.setString(4, "user" + i + "@example.com");
                        pstmt.addBatch();

                        if (i % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                        }
                    }
                    pstmt.executeBatch();
                    conn.commit();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    private static void insertQuestions() throws InterruptedException {
        String sql = "INSERT INTO Question (title, contents, date, status, user_id) VALUES (?, ?, ?, ?, ?)";
        int totalQuestions = 500000;
        int questionsPerThread = totalQuestions / THREAD_COUNT;
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Random rand = new Random();

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            executorService.submit(() -> {
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    conn.setAutoCommit(false);
                    for (int i = 1 + threadId * questionsPerThread; i <= (threadId + 1) * questionsPerThread; i++) {
                        pstmt.setString(1, "질문 제목 " + i);
                        pstmt.setString(2, "질문 내용 " + i);
                        pstmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
                        pstmt.setBoolean(4, true);
                        pstmt.setLong(5, 1 + rand.nextInt(10000));
                        pstmt.addBatch();

                        if (i % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                        }
                    }
                    pstmt.executeBatch();
                    conn.commit();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    private static void insertReplies() throws SQLException, InterruptedException {
        String sql = "INSERT INTO Reply (user_id, question_id, contents, date) VALUES (?, ?, ?, ?)";
        int totalQuestions = 500000;
        int questionsPerThread = totalQuestions / THREAD_COUNT;
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Random rand = new Random();

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            executorService.submit(() -> {
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    conn.setAutoCommit(false);
                    for (int i = 1 + threadId * questionsPerThread; i <= (threadId + 1) * questionsPerThread; i++) {
                        int numReplies = rand.nextInt(11);
                        for (int j = 0; j < numReplies; j++) {
                            pstmt.setLong(1, 1 + rand.nextInt(10000));
                            pstmt.setLong(2, i);
                            pstmt.setString(3, "질문 " + i + "에 대한 댓글 " + (j + 1));
                            pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
                            pstmt.addBatch();

                            if (pstmt.getParameterMetaData().getParameterCount() % BATCH_SIZE == 0) {
                                pstmt.executeBatch();
                                conn.commit();
                            }
                        }
                    }
                    pstmt.executeBatch();
                    conn.commit();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    // 애플리케이션 종료 시 ExecutorService를 종료하는 메소드
    public static void shutdownExecutorService() {
        executorService.shutdown();
    }


    private static String readSqlFile(String resourcePath) throws IOException {
        try (InputStream is = JdbcTemplate.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
