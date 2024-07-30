package org.example.util;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BodyParser {

    public static byte[] byteBody(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength == 0) {
            return new byte[0];
        }

        byte[] body = new byte[contentLength];
        ServletInputStream inputStream = request.getInputStream();
        int bytesRead = 0;
        int offset = 0;
        // contentLength만큼 데이터를 읽어옴
        while (offset < contentLength) {
            bytesRead = inputStream.read(body, offset, contentLength - offset);
            if (bytesRead == -1) {
                break;
            }
            offset += bytesRead;
        }
        return body;
    }

    public static String stringBody(HttpServletRequest request) throws IOException {
        return new String(byteBody(request));
    }
}
