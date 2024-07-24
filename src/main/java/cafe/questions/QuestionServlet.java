package cafe.questions;

import cafe.MappingHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class QuestionServlet extends MappingHttpServlet {
    private final ArticleRepository articleRepository;

    @Override
    public List<String> mappings() {
        return List.of("/questions/*");
    }

    public QuestionServlet(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        req.setAttribute("article", article);
        req.getRequestDispatcher("/WEB-INF/views/questions/question.jsp").forward(req, resp);
    }
}
