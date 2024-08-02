package org.example.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BodyParserTest {

    @Test
    public void testByteBody_emptyRequest() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentLength()).thenReturn(0);

        byte[] result = BodyParser.byteBody(request);

        assertNotNull(result); // 결과가 null이 아니어야 합니다.
        assertEquals(0, result.length); // 길이가 0이어야 합니다.
    }

    @Test
    public void testByteBody_withContent() throws IOException {
        String content = "test content";
        byte[] contentBytes = content.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBytes);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentLength()).thenReturn(contentBytes.length);
        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return byteArrayInputStream.read(b, off, len);
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No-op
            }
        });

        byte[] result = BodyParser.byteBody(request);

        assertNotNull(result);
        assertArrayEquals(contentBytes, result); // 원래의 contentBytes와 동일해야 합니다.
    }

    @Test
    public void testStringBody_withContent() throws IOException {
        String content = "test content";
        byte[] contentBytes = content.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBytes);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentLength()).thenReturn(contentBytes.length);
        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return byteArrayInputStream.read(b, off, len);
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No-op
            }
        });

        String result = BodyParser.stringBody(request);

        assertNotNull(result);
        assertEquals(content, result); // 원래의 content와 동일한 문자열이어야 합니다.
    }
}
