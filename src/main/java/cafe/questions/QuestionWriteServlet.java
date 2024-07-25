package cafe.questions;

import cafe.MappingHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class QuestionWriteServlet extends MappingHttpServlet {
    private final ArticleRepository articleRepository;

    @Override
    public List<String> mappings() {
        return List.of("/question/write");
    }

    public QuestionWriteServlet(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/questions/write.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            req.setAttribute("errorMessage", "제목과 내용을 모두 입력해주세요.");
            req.setAttribute("title", title);
            req.setAttribute("content", content);
            req.getRequestDispatcher("/WEB-INF/views/questions/write.jsp").forward(req, resp);
            return;
        }

        // TODO: 사용자 id 를 어떻게 가져올 것인가?
        articleRepository.save(new Article(null, title, content));
        resp.sendRedirect(req.getContextPath() + "/questions");
    }
}
