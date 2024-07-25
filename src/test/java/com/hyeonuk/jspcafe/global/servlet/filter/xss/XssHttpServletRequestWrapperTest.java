package com.hyeonuk.jspcafe.global.servlet.filter.xss;

import com.hyeonuk.jspcafe.global.exception.HttpInternalServerErrorException;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletRequest;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("XssHttpServletRequestWrapper 클래스")
class XssHttpServletRequestWrapperTest {

    private MockRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockRequest = new MockRequest();
    }

    @Nested
    @DisplayName("POST 요청과 application/json으로 요청을 받으면")
    class PostAndApplicationJson {
        @Test
        @DisplayName("application/json을 Content-Type으로 지정하고 파싱")
        void createWithPostAndJson() throws IOException {
            mockRequest.setMethod("POST");
            mockRequest.setContentType("application/json");
            String jsonBody = "{\"key\":\"<script>alert('xss')</script>\"}";
            mockRequest.setContent(jsonBody.getBytes());

            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            assertNotNull(wrapper);
            String filteredContent = new String(wrapper.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("{\"key\":\"&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;\"}",filteredContent);
        }
    }

    @Nested
    @DisplayName("POST 요청과 Multipart/form-data 요청을 받으면")
    class PostAndMultipartFormData {
        @Test
        @DisplayName("내부의 inputStream을 filtering")
        void createWithPostAndMultipart() throws IOException {
            mockRequest.setMethod("POST");
            mockRequest.setContentType("multipart/form-data");
            String body = "--boundary\r\nContent-Disposition: form-data; name=\"field\"\r\n\r\n<script>alert('xss')</script>\r\n--boundary--";
            mockRequest.setContent(body.getBytes());

            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            assertNotNull(wrapper);
            String filteredContent = new String(wrapper.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("--boundary\r\nContent-Disposition: form-data; name=\"field\"\r\n\r\n&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;\r\n--boundary--",filteredContent);
        }
    }
    @Nested
    @DisplayName("Get 요청을 받으면")
    class Get {
        @Test
        @DisplayName("GET 요청으로 생성")
        void createWithGet() {
            mockRequest.setMethod("GET");

            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            assertNotNull(wrapper);
        }
    }

        @Test
        @DisplayName("IOException 발생 시 HttpInternalServerErrorException 던짐")
        void throwsHttpInternalServerErrorExceptionOnIOException() {
            mockRequest.setMethod("POST");
            mockRequest.setContentType("application/json");
            mockRequest.setThrowIOException(true);

            assertThrows(HttpInternalServerErrorException.class, () -> new XssHttpServletRequestWrapper(mockRequest));
        }

    @Nested
    @DisplayName("메소드")
    class Methods {
        @Test
        @DisplayName("getQueryString 메소드")
        void getQueryString() {
            mockRequest.setQueryString("param=<script>alert('xss')</script>");
            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            String result = wrapper.getQueryString();

            assertEquals("param=&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;", result);
        }

        @Test
        @DisplayName("getParameter 메소드")
        void getParameter() {
            mockRequest.setParameter("param", "<script>alert('xss')</script>");
            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            String result = wrapper.getParameter("param");

            assertEquals("&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;", result);
        }

        @Test
        @DisplayName("getParameterMap 메소드")
        void getParameterMap() {
            Map<String, String[]> parameterMap = new HashMap<>();
            parameterMap.put("param", new String[]{"<script>alert('xss')</script>"});
            mockRequest.setParameterMap(parameterMap);
            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            Map<String, String[]> result = wrapper.getParameterMap();

            assertEquals("&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;", result.get("param")[0]);
        }

        @Test
        @DisplayName("getParameterValues 메소드")
        void getParameterValues() {
            mockRequest.setParameterValues("param", new String[]{"<script>alert('xss')</script>"});
            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            String[] result = wrapper.getParameterValues("param");

            assertEquals("&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;", result[0]);
        }

        @Test
        @DisplayName("getReader 메소드")
        void getReader() throws IOException {
            mockRequest.setMethod("POST");
            mockRequest.setContentType("application/json");
            String jsonBody = "{\"key\":\"<script>alert('xss')</script>\"}";
            mockRequest.setContent(jsonBody.getBytes());

            XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(mockRequest);

            BufferedReader reader = wrapper.getReader();
            String line = reader.readLine();

            assertEquals("{\"key\":\"&lt;script&gt;alert&#40;'xss'&#41;&lt;/script&gt;\"}",line);
        }
    }

    private static class MockRequest extends BaseHttpServletRequest {
        private String method;
        private String contentType;
        private byte[] content;
        private String queryString;
        private Map<String, String[]> parameterMap = new HashMap<>();
        private boolean throwIOException = false;

        public void setMethod(String method) {
            this.method = method;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public void setParameter(String name, String value) {
            this.parameterMap.put(name, new String[]{value});
        }

        public void setParameterMap(Map<String, String[]> parameterMap) {
            this.parameterMap = parameterMap;
        }

        public void setParameterValues(String name, String[] values) {
            this.parameterMap.put(name, values);
        }

        public void setThrowIOException(boolean throwIOException) {
            this.throwIOException = throwIOException;
        }

        @Override
        public String getMethod() {
            return method;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (throwIOException) {
                throw new IOException("Test IOException");
            }
            return new ServletInputStream() {
                private ByteArrayInputStream bais = new ByteArrayInputStream(content);

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }

        @Override
        public String getQueryString() {
            return queryString;
        }

        @Override
        public String getParameter(String name) {
            String[] values = parameterMap.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return parameterMap;
        }

        @Override
        public String[] getParameterValues(String name) {
            return parameterMap.get(name);
        }
    }
}