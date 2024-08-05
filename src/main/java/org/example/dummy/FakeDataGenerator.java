package org.example.dummy;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.example.constance.AliveStatus;

public class FakeDataGenerator {
    private static final int USER_COUNT = 10_000;
    private static final int ARTICLE_COUNT = 500_000;
    private static final int MAX_REPLIES = 10;
    private static final String[] ALIVE_STATUS = {AliveStatus.ALIVE.name(), AliveStatus.DELETED.name()};

    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static Map<Long, String> userNicknames = new HashMap<>();
    private static Map<Long, String> articleStatus = new HashMap<>();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        generateUsers();
        System.out.println("generate user time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        generateArticles();
        System.out.println("generate article time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        generateReplies();
        System.out.println("generate replies time: " + (System.currentTimeMillis() - start));
    }

    private static void generateUsers() {
        try (FileWriter writer = new FileWriter("./users.csv")) {
            writer.write("user_id,email,nickname,password,created_dt\n");
            for (long i = 1; i <= USER_COUNT; i++) {
                String email = "email_" + i + "@naver.com";
                String nickname = "nickname_" + i;
                String password = "pw_" + i;
                String createdDt = LocalDateTime.now().minusDays(random.nextInt(365)).format(formatter);
                writer.write(String.format("%d,%s,%s,%s,%s\n", i, email, nickname, password, createdDt));
                userNicknames.put(i, nickname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateArticles() {
        try (FileWriter writer = new FileWriter("./articles.csv")) {
            writer.write("article_id,user_id,title,content,author,alive_status,created_dt\n");
            for (long i = 1; i <= ARTICLE_COUNT; i++) {
                long userId = random.nextInt(USER_COUNT) + 1;
                String title = "title_" + i;
                String content = "content_" + i;
                String author = userNicknames.get(userId);
                String aliveStatus = ALIVE_STATUS[random.nextInt(ALIVE_STATUS.length)];
                String createdDt = LocalDateTime.now().minusDays(random.nextInt(365)).format(formatter);
                writer.write(String.format("%d,%d,%s,%s,%s,%s,%s\n", i, userId, title, content, author, aliveStatus,
                        createdDt));
                articleStatus.put(i, aliveStatus);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateReplies() {
        try (FileWriter writer = new FileWriter("./replies.csv")) {
            writer.write("reply_id,user_id,article_id,author,comment,alive_status,created_dt\n");
            long replyId = 1;
            for (long articleId = 1; articleId <= ARTICLE_COUNT; articleId++) {
                int replyCount = getRandomReplyCount();
                for (int j = 0; j < replyCount; j++) {
                    long userId = random.nextInt(USER_COUNT) + 1;
                    String author = userNicknames.get(userId);
                    String comment = "comment_" + articleId + "_" + j;
                    String aliveStatus =
                            articleStatus.get(articleId).equals(AliveStatus.DELETED.name()) ? AliveStatus.DELETED.name()
                                    : ALIVE_STATUS[random.nextInt(ALIVE_STATUS.length)];
                    String createdDt = LocalDateTime.now().minusDays(random.nextInt(365)).format(formatter);
                    writer.write(String.format("%d,%d,%d,%s,%s,%s,%s\n", replyId++, userId, articleId, author, comment,
                            aliveStatus, createdDt));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getRandomReplyCount() {
        return random.nextInt(MAX_REPLIES + 1);
    }
}
