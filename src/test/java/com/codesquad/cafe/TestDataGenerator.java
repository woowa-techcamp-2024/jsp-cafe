package com.codesquad.cafe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {
    private static final String[] LAST_NAMES = {
            "김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "오", "한", "신", "서", "권", "황", "안", "송", "류", "전"
    };

    private static final String[] FIRST_NAMES = {
            "민준", "서준", "지훈", "준혁", "우진", "지우", "현우", "도현", "민서", "지호",
            "하준", "예준", "시우", "서진", "은우", "재원", "유준", "하율", "주원", "승우",
            "윤서", "준서", "동현", "건우", "서우", "유찬", "우빈", "승민", "지안", "지환",
            "예성", "태윤", "지원", "성민", "준우", "현서", "연우", "재민", "시윤", "하람",
            "지한", "수현", "이안", "서윤", "태민", "예찬", "도윤", "시후", "민혁", "하진",
            "지성", "현준", "승현", "은찬", "지후", "민찬", "현수", "우현", "예림", "성현",
            "윤호", "서율", "동훈", "태훈", "재윤", "하민", "건희", "승호", "유민", "찬우",
            "민성", "은호", "성우", "주호", "태우", "지혁", "현승", "진우", "승준", "태영",
            "정우", "태준", "지율", "예람", "도경", "우성", "도운", "현빈", "주안", "승훈",
            "수호", "시영", "태경", "진혁", "건율", "지훈", "민재", "재영", "성찬", "태성"
    };

    private static final Random RANDOM = new Random();

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("users.csv", false));) {
            LocalDateTime startDate = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2023, 7, 31, 23, 59);
            for (int i = 0; i < 10000; i++) {
                StringBuilder sb = new StringBuilder();
                //1. id
                sb.append((i + 1));
                sb.append(",");
                //2. username
                String username = "woowa" + (i + 1);
                sb.append(username);
                sb.append(",");
                //3. password
                sb.append("woowapw" + (i + 1));
                sb.append(",");
                //4.name
                sb.append(generateRandomKoreanName());
                sb.append(",");
                //5.email
                sb.append(username);
                sb.append("@gmail.com");
                sb.append(",");
                //6.createdAt
                String date = generateRandomDate(startDate, endDate).format(DATE_FORMAT);
                sb.append(date);
                sb.append(",");
                //7.updatedAt
                sb.append(date);
                sb.append(",");
                //8.deleted
                sb.append(0);
                sb.append('\n');
                fileWriter.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter postFileWriter = new BufferedWriter(new FileWriter("posts.csv", false));
             BufferedWriter commentFileWriter = new BufferedWriter(new FileWriter("comments.csv", false))) {
            LocalDateTime startDate = LocalDateTime.of(2023, 8, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 6, 30, 23, 59);
            LocalDateTime commentEndDate = LocalDateTime.of(2024, 7, 30, 23, 59);
            for (int i = 0; i < 500000; i++) {
                StringBuilder postSb = new StringBuilder();
                //1. id
                postSb.append((i + 1));
                postSb.append(",");
                //2. author_id
                postSb.append(RANDOM.nextInt(10000) + 1);
                postSb.append(",");
                //3. title
                String title = "title" + (i + 1);
                postSb.append(title);
                postSb.append(",");
                //4.content
                postSb.append("this is the content of post ");
                postSb.append(title);
                postSb.append(",");
                //5.view
                postSb.append(0);
                postSb.append(",");
                //6.createdAt
                LocalDateTime postCreatedAt = generateRandomDate(startDate, endDate);
                String formattedPostCreatedAt = postCreatedAt.format(DATE_FORMAT);
                postSb.append(postCreatedAt);
                postSb.append(",");
                //7.updatedAt
                postSb.append(postCreatedAt);
                postSb.append(",");
                //8.deleted
                postSb.append(0);
                postSb.append('\n');
                postFileWriter.write(postSb.toString());

                //---comment
                int commentCount = RANDOM.nextInt(10);
                for (int j = 0; j < commentCount; j++) {
                    StringBuilder commentSb = new StringBuilder();
                    //1. post_id
                    commentSb.append((i + 1));
                    commentSb.append(",");
                    //2. uesr_id
                    commentSb.append(RANDOM.nextInt(10000) + 1);
                    commentSb.append(",");
                    //3.content
                    commentSb.append("댓글 내용입니다.");
                    commentSb.append(",");
                    //4.createdAt
                    String commentCreatedAt = generateRandomDate(postCreatedAt, commentEndDate).format(DATE_FORMAT);
                    commentSb.append(commentCreatedAt);
                    commentSb.append(",");
                    //5.updatedAt
                    commentSb.append(commentCreatedAt);
                    commentSb.append(",");
                    //6.deleted
                    commentSb.append(0);
                    commentSb.append('\n');
                    commentFileWriter.write(commentSb.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomKoreanName() {
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        return lastName + firstName;
    }

    private static LocalDateTime generateRandomDate(LocalDateTime startDate, LocalDateTime endDate) {
        long startEpoch = startDate.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = endDate.toEpochSecond(ZoneOffset.UTC);
        long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);

        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC);
    }

}
