package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.QuestionService;
import camp.woowa.jspcafe.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(value = "/questions/*")
public class QuestionsServlet extends HttpServlet {
    private QuestionService questionService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();

        questionService = (QuestionService) sc.getAttribute("questionService");
        userService = (UserService) sc.getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            createQuestion(req, resp);
        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.endsWith("/form")) {
            String questionIdStr = pathInfo.split("/")[1];
            try { // questionId 검증
                Long questionId = Long.parseLong(questionIdStr);
                updateQuestions(req, resp, questionId);
            } catch (NumberFormatException e) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Question Id");
            }
        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    private void updateQuestions(HttpServletRequest req, HttpServletResponse resp, Long questionId) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            try {
                resp.sendRedirect("/user/login");
                return;
            } catch (IOException e) {
                log("Redirect Error", e);
            }
        }

        User user = (User) session.getAttribute("user");

        User w = findByUserIdOrThrow(user.getUserId());// Check if the user exists

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        questionService.update(questionId, title, content, w.getId());

        try {
            resp.sendRedirect("/");
        } catch (IOException e) {
            log("Redirect Error", e);
        }
    }

    private User findByUserIdOrThrow(String w) {
        User writer = userService.findByUserId(w);

        if (writer == null) {
            throw new CustomException(HttpStatus.USER_NOT_FOUND);
        } else {
            return writer;
        }
    }

    private void createQuestion(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession(false);

        if (session == null) {
            try {
                resp.sendRedirect("/user/login");
                return;
            } catch (IOException e) {
                log("Redirect Error", e);
            }
        }

        User user = (User) session.getAttribute("user");

        User w = findByUserIdOrThrow(user.getUserId());// Check if the user exists

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String writer = w.getUserId();

        questionService.save(title, content, writer, w.getId());


        try {
            resp.sendRedirect("/");
        } catch (IOException e) {
            log("Redirect Error", e);
        }
    }
}
