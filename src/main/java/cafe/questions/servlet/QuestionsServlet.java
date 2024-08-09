package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.repository.ArticleRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static cafe.questions.repository.JdbcArticleRepository.PAGE_SIZE;

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
        int currentPage = Optional.ofNullable(req.getParameter("page"))
                .map(Integer::parseInt)
                .filter(p -> p > 0)
                .orElse(1);

        long totalArticles = articleRepository.count();
        int totalPages = (int) ((totalArticles + totalArticles - 1) / PAGE_SIZE);

        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);

        List<Article> articleList = articleRepository.findAllPaginated(currentPage);
        req.setAttribute("articleList", articleList);

        req.getRequestDispatcher("/WEB-INF/views/questions/questionList.jsp").forward(req, resp);
    }

}
