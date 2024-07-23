package codesquad.jspcafe.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultHTMLData는")
class DefaultHTMLDataTest {

    @Test
    @DisplayName("중복되는 HTML 해드를 반환한다.")
    void getHtmlHead() {
        // Act
        String actualResult = DefaultHTMLData.getHtmlHead();
        // Assert
        assertThat(actualResult).isNotNull();
    }

    @Test
    @DisplayName("중복되는 HTML 네비 바를 반환한다.")
    void getNaviBar() {
        // Act
        String actualResult = DefaultHTMLData.getNaviBar();
        // Assert
        assertThat(actualResult).isNotNull();
    }

    @Test
    @DisplayName("중복되는 HTML 스크립트를 반환한다.")
    void getScript() {
        // Act
        String actualResult = DefaultHTMLData.getScripts();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}