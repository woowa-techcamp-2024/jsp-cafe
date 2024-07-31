package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.resolver.RequestParametersResolver;
import woopaca.jspcafe.service.ReplyService;
import woopaca.jspcafe.servlet.dto.request.WriteReplyRequest;

import java.io.IOException;

@WebServlet("/replies")
public class ReplyServlet extends HttpServlet {

    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.replyService = (ReplyService) servletContext.getAttribute("replyService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WriteReplyRequest writeReplyRequest =
                RequestParametersResolver.resolve(request.getParameterMap(), WriteReplyRequest.class);
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        replyService.writeReply(writeReplyRequest, authentication);
        response.sendRedirect("/posts/" + writeReplyRequest.postId());
    }
}
