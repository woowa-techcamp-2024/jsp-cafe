package cafe.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDtoTest {
    @Test
    void 올바른_인자가_들어간_객체를_생성한다() {
        // given
        String title = "title";
        String contents = "contents";

        // when
        ArticleDto articleDto = new ArticleDto(title, contents);

        // then
        assertEquals(articleDto.getTitle(), title);
        assertEquals(articleDto.getContents(), contents);
    }
}