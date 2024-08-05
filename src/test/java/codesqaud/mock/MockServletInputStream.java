package codesqaud.mock;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MockServletInputStream extends ServletInputStream {
    private ByteArrayInputStream byteArrayInputStream;

    public void setInputStream(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        this.byteArrayInputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean isFinished() {
        return byteArrayInputStream == null || byteArrayInputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        if (byteArrayInputStream == null) {
            return -1;
        }
        return byteArrayInputStream.read();
    }
}
