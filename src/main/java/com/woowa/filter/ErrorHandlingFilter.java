package com.woowa.filter;

import com.woowa.exception.AuthenticationException;
import com.woowa.exception.AuthorizationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ErrorHandlingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            handleException(e, (HttpServletRequest) request, (HttpServletResponse) response);
        }
    }

    private void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(e instanceof IllegalArgumentException) {  // 400
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/WEB-INF/classes/static/error/bad_request.jsp");
            dispatcher.forward(request, response);
        } else if(e instanceof AuthenticationException) {  // 401 로그인 리다이렉트
            response.sendRedirect("/login");
        } else if(e instanceof AuthorizationException) {  // 403
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/WEB-INF/classes/static/error/authorization.jsp");
            dispatcher.forward(request, response);
        } else if(e instanceof NoSuchElementException) {  // 404
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    "/WEB-INF/classes/static/error/not_found.jsp");
            dispatcher.forward(request, response);
        } else {  // 500 그 외의 에러
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/classes/static/error/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
