package woopaca.jspcafe.resolver;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import woopaca.jspcafe.error.BadRequestException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestParametersResolverTest {

    @Nested
    class resolve_메서드는 {

        record TestRecord(String key) {
        }

        @Nested
        class 유효한_요청_파라미터 {

            @Test
            void 정상적으로_파라미터를_레코드_클래스로_변환한다() {
                Map<String, String[]> parameters = Map.of("key", new String[]{"value"});
                TestRecord testRecord = RequestParametersResolver.resolve(parameters, TestRecord.class);
                assertThat(testRecord).isNotNull();
                assertThat(testRecord.key()).isEqualTo("value");
            }
        }

        @Nested
        class 유효하지_않은_요청_파라미터 {

            @Test
            void 요청_파라미터에_값이_없으면_예외가_발생한다() {
                Map<String, String[]> parameters = Map.of("invalid", new String[]{});
                assertThatThrownBy(() -> RequestParametersResolver.resolve(parameters, TestRecord.class))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("[ERROR] 파라미터에 `key` 값이 없습니다.");
            }
        }
    }
}