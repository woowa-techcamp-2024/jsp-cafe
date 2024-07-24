package codesquad.jspcafe.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DateTimeFormatExecutor는")
class DateTimeFormatExecutorTest {

    @Test
    @DisplayName("LocalDateTime을 yyyy-MM-dd HH:mm 형식으로 변환한다.")
    void execute() {
        // Arrange
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        String expectedResult = expectedLocalDateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // Act
        String actualResult = DateTimeFormatExecutor.execute(expectedLocalDateTime);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}