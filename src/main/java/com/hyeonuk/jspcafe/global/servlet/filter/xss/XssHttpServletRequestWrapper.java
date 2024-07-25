package com.hyeonuk.jspcafe.global.servlet.filter.xss;

import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] raw;//inputstream의 데이터를 필터링하기 위한 맴버변수(wrapper는 request별로 각자 new되기 때문에 동시성문제 고려 x)

    private String xssFilter(String input) {
        if (input != null) {
            input = input.replaceAll("\\<", "&lt;")
                    .replaceAll("\\>", "&gt;")
                    .replaceAll("\\(", "&#40;")
                    .replaceAll("\\)", "&#41;");
        }
        return input;
    }

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            if ("post".equalsIgnoreCase(request.getMethod())
                    && "application/json".equalsIgnoreCase(request.getContentType())
                    || "multipart/form-data".equalsIgnoreCase(request.getContentType())) {

                InputStream is = request.getInputStream();

                StringBuffer sb = new StringBuffer();
                try (Reader reader = new BufferedReader(new InputStreamReader
                        (is, StandardCharsets.UTF_8))) {
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        sb.append((char) c);
                    }
                }
                this.raw = xssFilter(sb.toString()).getBytes();
            }
        } catch (IOException e) {
            throw new HttpInternalServerErrorException("서버 오류");
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.raw == null) {
            return super.getInputStream();
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(this.raw);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return bais.available() != 0;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public String getQueryString() {
        return xssFilter(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return xssFilter(super.getParameter(name));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> params = super.getParameterMap();
        if (params != null) {
            params.forEach((key, value) -> {
                for (int i = 0; i < value.length; i++) {
                    value[i] = xssFilter(value[i]);
                }
            });
        }
        return params;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] params = super.getParameterValues(name);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                params[i] = xssFilter(params[i]);
            }
        }
        return params;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }
}
