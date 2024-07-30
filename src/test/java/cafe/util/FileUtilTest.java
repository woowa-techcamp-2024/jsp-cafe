package cafe.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileUtilTest {
    @BeforeAll
    static void setUp() {
        File file = new File("src/test/resources/test.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void 올바른_경로에_파일을_생성한다() {
        // given
        String path = "src/test/resources/test.txt";

        // when
        FileUtil.createFile(path);

        // then
        assertTrue(new File(path).exists());
    }

    @Test
    void 확장자명이_없으면_예외가_발생한다() {
        // given
        String path = "src/test/resources/test";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> FileUtil.createFile(path));
    }

    @Test
    void 경로가_null일_경우_예외가_발생한다() {
        // given
        String path = null;

        // when, then
        assertThrows(IllegalArgumentException.class, () -> FileUtil.createFile(path));
    }
}