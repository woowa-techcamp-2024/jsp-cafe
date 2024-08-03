package codesquad.article.handler;

import codesquad.article.service.RegisterArticleService;
import codesquad.article.service.RegisterArticleService.Command;
import codesquad.common.handler.RequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RequestMapping("/questions")
public class QnasHandler extends HttpServlet implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(QnasHandler.class);

    private final RegisterArticleService registerArticleService;

    public QnasHandler(RegisterArticleService registerArticleService) {
        this.registerArticleService = registerArticleService;
    }

    /**
     * 질문 등록 요청
     */
    @Authorized
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("uploading article");
        String title = req.getParameter("title");
        String writer = ((User) req.getSession().getAttribute("loginUser")).getUserId();
        String content = req.getParameter("contents");

        Command command = new Command(title, writer, content);
        registerArticleService.register(command);

        resp.sendRedirect(req.getContextPath() + "/");
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
