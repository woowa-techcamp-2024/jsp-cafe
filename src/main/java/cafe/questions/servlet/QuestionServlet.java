package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.repository.ArticleRepository;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class QuestionServlet extends MappingHttpServlet {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    public List<String> mappings() {
        return List.of("/questions/*");
    }

    public QuestionServlet(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        req.setAttribute("article", article);
        if (article.getUserId() != null || article.getUserId() != 0) {
            req.setAttribute("user", userRepository.findById(article.getUserId()));
        }
        req.getRequestDispatcher("/WEB-INF/views/questions/question.jsp").forward(req, resp);
    }
}
