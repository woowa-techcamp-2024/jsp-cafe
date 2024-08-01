package codesquad.jspcafe.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("ApplicationProperties는")
class ApplicationPropertiesTest {

    private final ApplicationProperties applicationProperties = new ApplicationProperties();

    @Test
    @DisplayName("jdbcUrl을 반환한다.")
    void getJdbcUrl() {
        // Arrange
        String expectedResult = "jdbc:mysql://localhost:3306/test";
        // Act
        String actualResult = applicationProperties.getJdbcUrl();
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("jdbcUsername을 반환한다.")
    void getJdbcUsername() {
        // Arrange
        String expectedResult = "test";
        // Act
        String actualResult = applicationProperties.getJdbcUsername();
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("jdbcPassword을 반환한다.")
    void getJdbcPassword() {
        // Arrange
        String expectedResult = "test!";
        // Act
        String actualResult = applicationProperties.getJdbcPassword();
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}