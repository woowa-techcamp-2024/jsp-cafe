package com.wootecam.jspcafe.servlet.user;

import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignupServlet extends AbstractHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SignupServlet.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        log.debug("forward to signup form");

        req.getRequestDispatcher("/WEB-INF/views/user/form.jsp")
                .forward(req, resp);
    }
}
