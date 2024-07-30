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

@WebServlet(value = "/questions")
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
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect("/user/login");
            return;
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

    private User findByUserIdOrThrow(String w) {
        User writer = userService.findByUserId(w);

        if (writer == null) {
            throw new CustomException(HttpStatus.USER_NOT_FOUND);
        } else {
            return writer;
        }
    }
}
