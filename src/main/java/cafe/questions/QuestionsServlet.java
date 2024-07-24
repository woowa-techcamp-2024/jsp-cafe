package cafe.questions;

import cafe.MappingHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class QuestionsServlet extends MappingHttpServlet {
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
        req.setAttribute("articleList", articleRepository.findAll());
        req.getRequestDispatcher("/WEB-INF/views/questions/questionList.jsp").forward(req, resp);
    }

}
