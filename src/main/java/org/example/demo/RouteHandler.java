package org.example.demo;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface RouteHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException;
}