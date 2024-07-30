package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.repository.ArticleRepository;
import cafe.users.User;
import cafe.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

public class QuestionServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(QuestionServlet.class.getName());
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


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        if (validAutorized(req, resp, article)) return;

        String title, content;

        try {
            LinkedHashMap<String, Object> parsedObject = new JSONParser(req.getReader()).parseObject();
            title = (String) parsedObject.get("title");
            content = (String) parsedObject.get("content");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (title == null || content == null ||
                title.isBlank() || content.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("title and content are required");
            return;
        }

        articleRepository.save(article.withTitle(title).withContent(content));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        if (validAutorized(req, resp, articleRepository.findById(id))) return;
        articleRepository.deleteById(id);
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
