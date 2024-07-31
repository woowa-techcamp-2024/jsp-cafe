package cafe.domain.util;

import org.junit.jupiter.api.Test;

public class SQLExecutorTest {

    @Test
    void 하나의_객체만_생성한다() {
        // given
        SQLExecutor sqlExecutor1 = SQLExecutor.getInstance();
        SQLExecutor sqlExecutor2 = SQLExecutor.getInstance();

        // when, then
        assert sqlExecutor1 == sqlExecutor2;
    }
}
