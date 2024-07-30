package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.ReplyService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static utils.SessionUtils.getSessionUser;

@WebServlet("/replies/*")
public class RepliesServlet extends HttpServlet {
    private ReplyService replyService;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        replyService = (ReplyService) sc.getAttribute("replyService");
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

    private void createReply(HttpServletRequest req, HttpServletResponse resp) {
        try {
        User sessionUser = getSessionUser(req, resp);
            if (sessionUser == null) { // 로그인 검증
                resp.sendRedirect("/users/login");
                return;
            }

            Long questionId = Long.parseLong(req.getParameter("questionId"));
            String content = req.getParameter("content");


            replyService.createReply(questionId, sessionUser.getId(), sessionUser.getName(), content);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"result\":\"success\"}");
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (NumberFormatException e) {
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
            resp.setContentType("application/json");
            resp.getWriter().write("{\"replies\":" + replies.toString() + "}");
        } catch (IOException | NumberFormatException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
