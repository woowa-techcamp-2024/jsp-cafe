package com.woowa.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.exception.AuthenticationException;
import com.woowa.exception.AuthorizationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class ErrorHandlingFilter implements Filter {

    private final ObjectMapper objectMapper;

    public ErrorHandlingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            String contentType = response.getContentType();
            if(contentType != null && contentType.contains("application/json")) {
                handleJsonException(e, (HttpServletRequest) request, (HttpServletResponse) response);
            } else {
                handleException(e, new HttpMethodRequestWrapper((HttpServletRequest) request, "GET"),
                        (HttpServletResponse) response);
            }
        }
    }

    private void handleJsonException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String errorMessage = e.getMessage();
        if(e instanceof IllegalArgumentException) {  // 400
            response.setStatus(400);
        } else if(e instanceof AuthenticationException) {  // 401 로그인 리다이렉트
            response.setStatus(401);
        } else if(e instanceof AuthorizationException) {  // 403
            response.setStatus(403);
        } else if(e instanceof NoSuchElementException) {  // 404
            response.setStatus(404);
        } else {  // 500 그 외의 에러
            response.setStatus(500);
            errorMessage = "예측하지 못한 예외가 발생했습니다.";
        }
        String jsonResponse = objectMapper.writeValueAsString(new ErrorResponse(errorMessage));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(jsonResponse.getBytes().length);
        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
        writer.close();
    }

    private record ErrorResponse(String message) {

    }

    private void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(e instanceof IllegalArgumentException) {  // 400
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/error/bad_request.jsp");
            dispatcher.forward(request, response);
        } else if(e instanceof AuthenticationException) {  // 401 로그인 리다이렉트
            response.sendRedirect("/login");
        } else if(e instanceof AuthorizationException) {  // 403
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/error/authorization.jsp");
            dispatcher.forward(request, response);
        } else if(e instanceof NoSuchElementException) {  // 404
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/error/not_found.jsp");
            dispatcher.forward(request, response);
        } else {  // 500 그 외의 에러
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method.toUpperCase();
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
