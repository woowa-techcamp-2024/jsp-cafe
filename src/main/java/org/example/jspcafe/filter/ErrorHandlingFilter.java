package org.example.jspcafe.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(filterName = "ErrorHandlingFilter", urlPatterns = {"/*"})
public class ErrorHandlingFilter implements Filter {
    private String paramName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 시 초기화 매개변수 가져오기
        paramName = filterConfig.getInitParameter("paramName");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // 다음 필터 또는 서블릿으로 요청 전달
            chain.doFilter(request, response);
        } catch (Exception ex) {
            // 예외 처리
            // 예외가 발생했을 때 원래 요청의 URL로 포워딩
            ex.printStackTrace();
            String originalUrl = httpRequest.getRequestURI();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
