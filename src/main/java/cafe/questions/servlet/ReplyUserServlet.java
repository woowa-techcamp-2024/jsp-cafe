package cafe.questions.servlet;

import cafe.MappingHttpServlet;
import cafe.questions.Reply;
import cafe.questions.repository.ReplyRepository;
import cafe.users.User;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ReplyUserServlet extends MappingHttpServlet {
    private final ReplyRepository replyRepository;
    private final Gson gson = new Gson();

    @Override
    public List<String> mappings() {
        return List.of("/replies/users/*");
    }

    public ReplyUserServlet(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        List<Reply> replyList = replyRepository.findByArticleIdAndUserId(id, user.getId());
        resp.setContentType("application/json");
        gson.toJson(replyList, resp.getWriter());
    }
}
