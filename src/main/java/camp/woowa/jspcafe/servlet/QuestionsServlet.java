package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.QuestionService;
import camp.woowa.jspcafe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.split("/").length == 2) {
            String questionIdStr = pathInfo.split("/")[1];
            try { // questionId 검증
                Long questionId = Long.parseLong(questionIdStr);
                deleteQuestions(req, resp, questionId);
            } catch (NumberFormatException e) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Question Id");
            }
        } else {
            throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    private User getSessionUser(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            return null;
        }

        User user = (User) session.getAttribute("user");

        return findByUserIdOrThrow(user.getUserId());
    }

    private void deleteQuestions(HttpServletRequest req, HttpServletResponse resp, Long questionId) {
        User w = getSessionUser(req, resp);
        if (w == null) {
            try {
                resp.sendRedirect("/user/login");
                return;
            } catch (IOException e) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to redirect");
            }
        }

        try {
            questionService.deleteById(questionId, w.getId());
        } catch (CustomException e) {
            resp.setStatus(e.getStatusCode());
            resp.setContentType("application/json");
            try {
                resp.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
                return;
            } catch (IOException ex) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write response");
            }
        }

        try {
            resp.setContentType("application/json");
            resp.getWriter().write("{\"result\":\"success\"}");
            return;
        } catch (IOException e) {
            log("Redirect Error", e);
        }
    }

    private void updateQuestions(HttpServletRequest req, HttpServletResponse resp, Long questionId) {
        User w = getSessionUser(req, resp);
        if (w == null) {
            try {
                resp.sendRedirect("/user/login");
                return;
            } catch (IOException e) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to redirect");
            }
        }
        // PUT request json body to Object
        try (BufferedReader reader = req.getReader()){
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> dataMap = mapper.readValue(reader, Map.class);

            String title = dataMap.get("title");
            String content = dataMap.get("content");

            questionService.update(questionId, title, content, w.getId());
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read request body");
        } catch (CustomException e) {
            resp.setStatus(e.getStatusCode());
            resp.setContentType("application/json");
            try {
                resp.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
                return;
            } catch (IOException ex) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write response");
            }
        }

        try {
            resp.setContentType("application/json");
            resp.getWriter().write("{\"result\":\"success\"}");
            return;
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
        User w = getSessionUser(req, resp);

        if (w == null) {
            try {
                resp.sendRedirect("/user/login");
                return;
            } catch (IOException e) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to redirect");
            }
        }

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
