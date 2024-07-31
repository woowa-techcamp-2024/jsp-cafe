package com.codesquad.cafe.filter;

import com.codesquad.cafe.exception.AuthenticationException;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ModelMappingException;
import com.codesquad.cafe.exception.ValidationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionTranslationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (ModelMappingException | ValidationException e) {
            sendError(resp, 400);
        } catch (AuthenticationException e) {
            req.getSession(true).setAttribute("redirectAfterLogin", req.getRequestURI().toString());
            resp.sendRedirect("/login");
        } catch (AuthorizationException e) {
            sendError(resp, 403);
        } catch (Exception e) {
            sendError(resp, 500);
        }
    }

    private void sendError(HttpServletResponse resp, int code) throws IOException {
        resp.sendError(code);
    }

}
