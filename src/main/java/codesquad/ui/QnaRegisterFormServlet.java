package codesquad.ui;

import codesquad.ui.annotation.authentication.Authorized;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/qna/register-form")
public class QnaRegisterFormServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaRegisterFormServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("QnaRegisterFormServlet initialized");
    }

    /**
     * 질문 등록 폼 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp").forward(req, resp);
    }
}
