package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Reply;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class RepliesServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;

    @Override
    public List<String> mappings() {
        return List.of("/replies");
    }

    public RepliesServlet(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        Long articleId = Long.parseLong(req.getParameter("articleId"));
        String content = req.getParameter("content");
        Reply reply = new Reply(articleId, user.getId(), content);
        Reply save = replyRepository.save(reply);
        resp.sendRedirect("/questions/" + articleId);
    }
}
