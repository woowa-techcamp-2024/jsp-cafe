package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.cafe.dto.QuestionInfo;
import woowa.cafe.dto.request.CreateQuestionRequest;
import woowa.cafe.service.QnaService;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;

import java.util.List;

@Router
public class QnaRouter {

    private final QnaService qnaService;

    public QnaRouter(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @HttpMapping(method = "GET", urlTemplate = "/")
    public String showQuestions(HttpServletRequest request, HttpServletResponse response) {
        List<QuestionInfo> questions = qnaService.getQuestions();
        request.setAttribute("questions", questions);
        return "/template/qna/list.jsp";
    }

    @HttpMapping(method = "GET", urlTemplate = "/question")
    public String showQuestionForm(HttpServletRequest request, HttpServletResponse response) {
        return "/template/qna/form.jsp";
    }

    @HttpMapping(method = "POST", urlTemplate = "/question")
    public String createQuestion(HttpServletRequest request, HttpServletResponse response) {
        CreateQuestionRequest req = new CreateQuestionRequest(
                request.getParameter("writer"),
                request.getParameter("title"),
                request.getParameter("contents")
        );

        try {
            qnaService.createQna(req);
            return "redirect:/";
        } catch (RuntimeException ex) {
            return "redirect:/question";
        }
    }

    @HttpMapping(method = "GET", urlTemplate = "/question/{id}")
    public String getQuestion(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getRequestURI().substring(10);
        QuestionInfo question = qnaService.getQuestion(id);

        if (question == null) {
            return "redirect:/question";
        }

        request.setAttribute("question", question);
        return "/template/qna/detail.jsp";
    }
}
