package woopaca.jspcafe.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import woopaca.jspcafe.servlet.dto.response.ReplyResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/replies/*")
public class RepliesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.replyService = (ReplyService) servletContext.getAttribute("replyService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = request.getReader()
                .lines()
                .collect(Collectors.joining());
        WriteReplyRequest writeReplyRequest = objectMapper.readValue(requestBody, WriteReplyRequest.class);
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        ReplyResponse replyResponse = replyService.writeReply(writeReplyRequest, authentication);
        response.setContentType("application/json");
        String responseBody = objectMapper.writeValueAsString(replyResponse);
        response.getWriter()
                .write(responseBody);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        Long replyId = Long.parseLong(pathInfo.substring(1));
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        replyService.deleteReply(replyId, authentication);
    }
}
