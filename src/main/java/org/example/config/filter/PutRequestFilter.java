package org.example.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.*;

@WebFilter("/*")
public class PutRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String method = httpRequest.getMethod();
            if ("PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)) {
                request = new PutRequestWrapper(httpRequest);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private static class PutRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String[]> params = new HashMap<>();

        public PutRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            parseParameters(requestBody.toString());
        }

        private void parseParameters(String requestBody) {
            String[] pairs = requestBody.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    try {
                        key = java.net.URLDecoder.decode(key, "UTF-8");
                        value = java.net.URLDecoder.decode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // Handle exception
                    }
                    String[] values = params.get(key);
                    if (values == null) {
                        values = new String[]{value};
                    } else {
                        String[] newValues = Arrays.copyOf(values, values.length + 1);
                        newValues[values.length] = value;
                        values = newValues;
                    }
                    params.put(key, values);
                }
            }
        }

        @Override
        public String getParameter(String name) {
            String[] values = params.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return new HashMap<>(params);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(params.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }
    }
}