package codesquad.auth.handler;

import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response
@RequestMapping("/logout")
public class LogoutHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    /**
     * 로그아웃 요청
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("logging out");
        req.getSession().invalidate();
        resp.sendRedirect("/");
    }
}
