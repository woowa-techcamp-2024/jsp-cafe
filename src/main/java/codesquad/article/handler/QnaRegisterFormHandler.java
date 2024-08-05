package codesquad.article.handler;

import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.common.handler.annotation.Response;
import codesquad.common.handler.annotation.RequestMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response
@RequestMapping("/questions/register-form")
public class QnaRegisterFormHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(QnaRegisterFormHandler.class);

    /**
     * 질문 등록 폼 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("QnaRegisterForm serve");
        req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp").forward(req, resp);
    }
}
