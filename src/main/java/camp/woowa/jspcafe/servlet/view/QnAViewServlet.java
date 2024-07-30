package camp.woowa.jspcafe.servlet.view;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.QuestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(value = "/qna/*")
public class QnAViewServlet extends HttpServlet {
    private QuestionService questionService;

    @Override
    public void init() throws ServletException {
        questionService = (QuestionService) getServletContext().getAttribute("questionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.startsWith("/form")) {
            try {
                req.getRequestDispatcher("/WEB-INF/jsp/qna/form.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Exception Occurred", e);
            }
        } else if (pathInfo.startsWith("/show")) {
            String[] split = pathInfo.split("/show/");
            long questionId = 0;
            try {
                questionId = Long.parseLong(split[1]);
            } catch (NumberFormatException e) {
                log("Invalid Question Id", e);
                try {
                    resp.sendRedirect("/");
                } catch (IOException ioException) {
                    log("Exception Occurred", ioException);
                }
            }
            Question question = questionService.findById(questionId);
            req.setAttribute("question", question);
            try {
                req.getRequestDispatcher("/WEB-INF/jsp/qna/show.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Exception Occurred", e);
            }

        } else if (pathInfo.endsWith("/form")) {
            String questionIdStr = pathInfo.split("/")[1];
            try {
                HttpSession session = req.getSession(false);

                if (session == null) {
                    resp.sendRedirect("/user/login");
                    return;
                }

                User sessionUser = (User) session.getAttribute("user");

                Long questionId = Long.parseLong(questionIdStr);
                Question target = questionService.findById(questionId);
                if (!target.getWriterId().equals(sessionUser.getId())) { // 소유권 검증
                    throw new CustomException(HttpStatus.FORBIDDEN, "다른 사람의 글을 수정할 수 없습니다.");
                }

                req.setAttribute("question", target);

                req.getRequestDispatcher("/WEB-INF/jsp/qna/updateForm.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Question Id");
            } catch (ServletException | IOException e) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to forward request");
            }
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND, "Page Not Found");
        }
    }
}
