package com.jspcafe.error;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error/*")
public class ErrorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorCode = req.getPathInfo().replace("/", "");
        req.setAttribute("errorCode", errorCode);
        resp.setStatus(Integer.parseInt(errorCode));
        switch (errorCode) {
            case "302" -> req.setAttribute("errorMessage", "권한이 없습니다.");
            case "404" -> req.setAttribute("errorMessage", "페이지를 찾을 수 없습니다.");
            case "500" -> req.setAttribute("errorMessage", "서버에서 문제가 발생했습니다.");
            default -> req.setAttribute("errorMessage", "문제가 발생했습니다.");
        }
        forward(req, resp);
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
    }
}
