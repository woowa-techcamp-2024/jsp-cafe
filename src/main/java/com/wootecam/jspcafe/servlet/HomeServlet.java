package com.wootecam.jspcafe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        log.debug("forward to home");

        req.getRequestDispatcher("/WEB-INF/views/index.jsp")
                .forward(req, resp);
    }
}
