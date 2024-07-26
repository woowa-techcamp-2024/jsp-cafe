package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.repository.ArticleRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class QuestionsServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(QuestionsServlet.class.getName());
    private final ArticleRepository articleRepository;

    @Override
    public List<String> mappings() {
        return List.of("/questions");
    }

    public QuestionsServlet(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Article> all = articleRepository.findAll();
        log.info("all: " + all);
        req.setAttribute("articleList", all);
        req.getRequestDispatcher("/WEB-INF/views/questions/questionList.jsp").forward(req, resp);
    }

}
