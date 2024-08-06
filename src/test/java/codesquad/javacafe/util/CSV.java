package codesquad.javacafe.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class CSV {

    @Test
    void csvUser() throws IOException {
        File file = new File("./src/test/resources/user.csv");
        if(!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("id," + "member_id," + "member_password," + "member_name");
            bw.newLine();
            bw.flush();
            for (int i = 10000; i < 20000; i++) {
                String data = i + ",user" + i + ",password,member name " + i;
                bw.write(data);
                bw.newLine();
            }
            bw.flush();
        } finally {
            bw.close();
        }
    }

    @Test
    void csvPost() throws IOException {
        File file = new File("./src/test/resources/post.csv");
        if(!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("id," +"post_writer,"+ "post_title,"+"post_contents,"+"post_create,"+"member_id");
            bw.newLine();
            bw.flush();

            Random random = new Random();
            int min = 10000;
            int max = 19999;
            for (int i = 10000; i < 510000; i++) {
                int randNum = random.nextInt((max - min)+1) + min;
                String data = i + ",user" + randNum + ",postTitle " + i + ",postContent " + i + "," + Timestamp.valueOf(LocalDateTime.now().plusMinutes(i-10000)) + ","+randNum;
                bw.write(data);
                bw.newLine();
            }
            bw.flush();
        } finally {
            bw.close();
        }
    }

    @Test
    void csvComment() throws IOException {
        File file = new File("./src/test/resources/comment.csv");
        if(!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("id," +"post_id,"+ "member_id,"+"comment_contents,"+"comment_create");
            bw.newLine();
            bw.flush();

            Random random = new Random();
            int memberMin = 10000;
            int memberMax = 19999;

            int commentMin = 0;
            int commentMax = 10;
            int commentId = 10000;
            for (int i = 10000; i < 510000; i++) {

                int commentRandom = random.nextInt((commentMax - commentMin)+1) + commentMin;
                for (int j = 0; j < commentRandom; j++) {
                    int memberRandom = random.nextInt((memberMax - memberMin)+1) + memberMin;
                    String data = (commentId++) + "," + i + ", " + memberRandom + ",comment " + i + "," + Timestamp.valueOf(LocalDateTime.now().plusSeconds(i-10000));
                    bw.write(data);
                    bw.newLine();
                }
            }
            bw.flush();
        } finally {
            bw.close();
        }
    }
}
