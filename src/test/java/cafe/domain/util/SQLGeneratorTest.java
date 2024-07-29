package cafe.domain.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
class SQLGeneratorTest {

    @Test
    void 하나의_객체만_생성한다() {
        // given
        SQLGenerator sqlGenerator1 = SQLGenerator.getInstance();
        SQLGenerator sqlGenerator2 = SQLGenerator.getInstance();

        // when, then
        assert sqlGenerator1 == sqlGenerator2;
    }

    @ParameterizedTest
    @MethodSource("createInsertTestData")
    void 올바른_삽입_구문을_생성한다(Object testData, String actualSQL) {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when
        String expectedSQL = sqlGenerator.generateInsertSQL("test_table", testData.getClass()
                .getDeclaredFields());

        // then
        assertEquals(expectedSQL, actualSQL);
    }

    private Stream<Arguments> createInsertTestData() {
        return Stream.of(
                Arguments.of(new OneArgumentTest(), "INSERT INTO `test_tables` (`id`, `column1`) VALUES (?, ?);"),
                Arguments.of(new TwoArgumentTest(), "INSERT INTO `test_tables` (`id`, `column1`, `column2`) VALUES (?, ?, ?);"),
                Arguments.of(new ThreeArgumentTest(), "INSERT INTO `test_tables` (`id`, `column1`, `column2`, `column3`) VALUES (?, ?, ?, ?);")
        );
    }

    @Test
    void 올바른_아이디_기반의_조회_구문을_생성한다() {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when
        String expectedSQL = sqlGenerator.generateSelectByIdSQL("test_table");

        // then
        assertEquals(expectedSQL, "SELECT * FROM `test_tables` WHERE `id` = ?;");
    }

    @Test
    void 올바른_조회_구문을_생성한다() {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when
        String expectedSQL = sqlGenerator.generateSelectAllSQL("test_table");

        // then
        assertEquals(expectedSQL, "SELECT * FROM `test_tables`;");
    }

    @ParameterizedTest
    @MethodSource("createUpdateTestData")
    void 올바른_수정_구문을_생성한다(Object testData, String actualSQL) {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when
        String expectedSQL = sqlGenerator.generateUpdateSQL("test_table", testData.getClass()
                .getDeclaredFields());

        // then
        assertEquals(expectedSQL, actualSQL);

    }

    private Stream<Arguments> createUpdateTestData() {
        return Stream.of(
                Arguments.of(new OneArgumentTest(), "UPDATE `test_tables` SET `column1` = ? WHERE `id` = ?;"),
                Arguments.of(new TwoArgumentTest(), "UPDATE `test_tables` SET `column1` = ?, `column2` = ? WHERE `id` = ?;"),
                Arguments.of(new ThreeArgumentTest(), "UPDATE `test_tables` SET `column1` = ?, `column2` = ?, `column3` = ? WHERE `id` = ?;")
        );
    }

    @Test
    void 클래스_이름이_null일_때_예외를_발생한다() {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateInsertSQL(null, new OneArgumentTest().getClass().getDeclaredFields()));
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateSelectByIdSQL(null));
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateSelectAllSQL(null));
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateUpdateSQL(null, new OneArgumentTest().getClass().getDeclaredFields()));
    }

    @Test
    void 필드가_null일_때_예외를_발생한다() {
        // given
        SQLGenerator sqlGenerator = SQLGenerator.getInstance();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateInsertSQL("test_table", null));
        assertThrows(IllegalArgumentException.class, () -> sqlGenerator.generateUpdateSQL("test_table", null));
    }

    private class OneArgumentTest {
        private String column1;
    }
    private class TwoArgumentTest {
        private String column1;
        private String column2;
    }
    private class ThreeArgumentTest {
        private String column1;
        private String column2;
        private String column3;
    }
}