package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Article;
import cafe.questions.Reply;
import cafe.questions.repository.ArticleRepository;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

public class QuestionServlet extends MappingHttpServlet {
    private static final Logger log = Logger.getLogger(QuestionServlet.class.getName());
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    @Override
    public List<String> mappings() {
        return List.of("/questions/*");
    }

    public QuestionServlet(ArticleRepository articleRepository, ReplyRepository replyRepository) {
        this.articleRepository = articleRepository;
        this.replyRepository = replyRepository;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        req.setAttribute("article", article);
        List<Reply> replyList = replyRepository.findByArticleId(id);
        req.setAttribute("replyList", replyList);
        req.getRequestDispatcher("/WEB-INF/views/questions/question.jsp").forward(req, resp);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Article article = articleRepository.findById(id);
        if (validAuthorized(req, resp, article)) return;

        String title, content;

        String requestBody = getRequestBody(req);

        LinkedHashMap<String, Object> parsedObject = new Gson().fromJson(requestBody, LinkedHashMap.class);
        title = (String) parsedObject.get("title");
        content = (String) parsedObject.get("content");

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
        if (validAuthorized(req, resp, articleRepository.findById(id))) return;

        List<Reply> replyList = replyRepository.findByArticleId(id);
        User user = (User) req.getSession().getAttribute("user");
        if (!replyList.stream().allMatch(reply -> reply.getUserId().equals(user.getId()))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("삭제할 수 없는 댓글이 있습니다.");
            return;
        }

        replyList.forEach(reply -> replyRepository.deleteById(reply.getId()));
        articleRepository.deleteById(id);
    }


    private boolean validAuthorized(HttpServletRequest req, HttpServletResponse resp, Article article) {
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

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
