package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.core.ServiceLocator;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.ReplyService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static camp.woowa.jspcafe.utils.SessionUtils.getSessionUser;

@WebServlet("/replies/*")
public class RepliesServlet extends HttpServlet {
    private final ReplyService replyService;

    public RepliesServlet() {
        replyService = ServiceLocator.getService(ReplyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            findByQuestionId(req, resp);
        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            createReply(req, resp);

        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.split("/").length == 2) {
            deleteReply(req, resp, pathInfo);
        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }

    private void deleteReply(HttpServletRequest req, HttpServletResponse resp, String pathInfo) {
        try {
            User sessionUser = getSessionUser(req, resp);

            if (sessionUser == null) { // 로그인 검증
                resp.sendRedirect("/users/login");
                return;
            }

            Long id = Long.parseLong(pathInfo.split("/")[1]);

            replyService.deleteById(id, sessionUser.getId());

            resp.setContentType("application/json");
            resp.getWriter().write("{\"result\":\"success\"}");
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }  catch (CustomException e) {
            resp.setStatus(e.getStatusCode());
            resp.setContentType("application/json");
            try {
                resp.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
                return;
            } catch (IOException ex) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write response");
            }
        }
    }

    private void createReply(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User sessionUser = getSessionUser(req, resp);
            if (sessionUser == null) { // 로그인 검증
                resp.sendRedirect("/users/login");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> dataMap = mapper.readValue(req.getReader(), new TypeReference<Map<String, Object>>() {});

            Long questionId =  ((Number) dataMap.get("questionId")).longValue();
            String content = (String) dataMap.get("content");


            replyService.createReply(questionId, sessionUser.getId(), sessionUser.getName(), content);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"result\":\"success\"}");
        } catch (IOException | NumberFormatException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void findByQuestionId(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User sessionUser = getSessionUser(req, resp);
            if (sessionUser == null) { // 로그인 검증
                resp.sendRedirect("/users/login");
                return;
            }

            Long questionId = Long.parseLong(req.getParameter("questionId"));
            List<Reply> replies = replyService.findByQuestionId(questionId);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), replies);
        } catch (IOException | NumberFormatException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
