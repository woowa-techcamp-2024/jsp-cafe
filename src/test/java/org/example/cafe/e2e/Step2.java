package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.getResponse;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.example.cafe.domain.Question;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Step2 extends TomcatBaseTestEnvironment {

    private HttpURLConnection con;

    @Test
    void 사용자는_게시글_목록을_조회할_수_있다() throws IOException {
        //given
        questionRepository.save(new Question("title1", "content1", "writer1"));
        questionRepository.save(new Question("title2", "content2", "writer2"));

        con = createGetConnection("/");

        //when
        con.connect();

        //then
        assertAll(() -> {
            assertThat(con.getResponseCode()).isEqualTo(200);
            assertThat(getResponse(con)).contains("title1", "title2", "writer1", "writer2");
        });
    }
}
