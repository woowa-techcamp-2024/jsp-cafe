package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.repository.ArticleRepository;
import cafe.users.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class QuestionEditServlet extends MappingHttpServlet {
    private final ArticleRepository articleRepository;

    @Override
    public List<String> mappings() {
        return List.of("/question/edit/*");
    }

    public QuestionEditServlet(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        if (validAutorized(req, resp, article)) return;
        req.setAttribute("article", article);
        req.getRequestDispatcher("/WEB-INF/views/questions/edit.jsp").forward(req, resp);
    }

    private boolean validAutorized(HttpServletRequest req, HttpServletResponse resp, Article article) {
        if (article == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return true;
        }

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !article.getUserId().equals(user.getId())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return true;
        }
        return false;
    }
}
