package com.codesquad.cafe.filter;

import com.codesquad.cafe.exception.AuthenticationException;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.MethodNotAllowedException;
import com.codesquad.cafe.exception.ModelMappingException;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.exception.ValidationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ExceptionTranslationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        ServletContext ctx = req.getServletContext();
        try {
            chain.doFilter(request, response);
        } catch (ModelMappingException | ValidationException e) {
            sendError(req, resp, 400);
        } catch (AuthenticationException e) {
            req.getSession(true).setAttribute("redirectAfterLogin", req.getRequestURI().toString());
            resp.sendRedirect("/login");
        } catch (AuthorizationException e) {
            sendError(req, resp, 403);
        } catch (ResourceNotFoundException e) {
            sendError(req, resp, 404);
        } catch (MethodNotAllowedException e) {
            sendError(req, resp, 405);
        } catch (Exception e) {
            sendError(req, resp, 500);
        }
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, int code)
            throws IOException, ServletException {
        resp.setStatus(code);
        resp.setContentType("text/html;charset=UTF-8");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                req.getServletContext().getResourceAsStream("/static/error/" + code + ".html")));
             PrintWriter writer = resp.getWriter()) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
        }
    }
}
