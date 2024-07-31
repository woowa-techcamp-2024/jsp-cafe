package com.wootecam.jspcafe.servlet;

import com.wootecam.jspcafe.exception.CommonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(AbstractHttpServlet.class);

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (CommonException e) {
            log.debug(e.getMessage(), e);
            responseError(req, resp, e);
        }
    }

    protected void responseError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final CommonException e) throws IOException, ServletException {
        request.setAttribute("errorResponse", e);
        request.getRequestDispatcher("/WEB-INF/views/error/error.jsp")
                .forward(request, response);
    }
}
