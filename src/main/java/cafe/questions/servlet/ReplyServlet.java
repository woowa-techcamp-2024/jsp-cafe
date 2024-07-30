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

public class ReplyServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;

    @Override
    public List<String> mappings() {
        return List.of("/replies/*");
    }

    public ReplyServlet(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        Reply reply = replyRepository.findById(id);

        if (reply == null || !reply.getArticleId().equals(user.getId())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("only the author can delete the reply");
            return;
        }

        replyRepository.deleteById(id);
    }
}
