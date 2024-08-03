package codesquad.user.handler;

import codesquad.common.handler.RequestHandler;
import codesquad.global.servlet.annotation.RequestMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RequestMapping("/users/register-form")
public class UserRegisterFormHandler extends HttpServlet implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserRegisterFormHandler.class);

    /**
     * 유저 등록 폼 요청
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("UserRegisterForm serve");
        req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
