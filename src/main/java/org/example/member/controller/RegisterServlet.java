package org.example.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/users")
public class RegisterServlet extends HttpServlet {

    private static final String USER_LIST_PAGE = "/user/list.html";
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getRequestDispatcher(USER_LIST_PAGE).forward(req, resp);
        } catch (ServletException | IOException e) {
            logger.error("Error occurred while processing request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 회원가입 로직. 회원 저장 후 redirect:/users
    }
}
