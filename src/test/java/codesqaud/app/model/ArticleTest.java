package codesqaud.app.model;

import codesqaud.app.exception.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ArticleTest {

    private String title = "title";
    private String contents = "Sample contents of the article.";
    private String authorId = "author123";

    @Nested
    class 생성자_유효성_검사 {
        @Test
        void title이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article(null, contents, authorId))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void title이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article("", contents, authorId))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void contents가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article(title, null, authorId))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void contents가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article(title, "", authorId))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void authorId가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article(title, contents, null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("작성자 ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void authorId가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Article(title, contents, ""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("작성자 ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void 올바른_값으로_생성된_Article는_정상적으로_생성된다() {
            Article article = new Article(title, contents, authorId);

            assertThat(article.getTitle()).isEqualTo(title);
            assertThat(article.getContents()).isEqualTo(contents);
            assertThat(article.getAuthorId()).isEqualTo(authorId);
        }
    }

    @Nested
    class Setter_유효성_검사 {
        private Article article;

        @BeforeEach
        void setUp() {
            article = new Article(title, contents, authorId);
        }

        @Test
        void title이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setTitle(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void title이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setTitle(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void contents가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setContents(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void contents가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setContents(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void authorId가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setAuthorId(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("작성자 ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void authorId가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setAuthorId(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("작성자 ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void 올바른_값으로_설정하면_정상적으로_반영된다() {
            article.setTitle("newTitle");
            article.setContents("newContents");
            article.setAuthorId("newAuthorId");

            assertThat(article.getTitle()).isEqualTo("newTitle");
            assertThat(article.getContents()).isEqualTo("newContents");
            assertThat(article.getAuthorId()).isEqualTo("newAuthorId");
        }
    }
}
