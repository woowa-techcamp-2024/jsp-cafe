package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.cafe.dto.QuestionInfo;
import woowa.cafe.dto.ReplyInfo;
import woowa.cafe.dto.UpdateQuestionRequest;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.dto.request.CreateQuestionRequest;
import woowa.cafe.service.QnaService;
import woowa.cafe.service.ReplyService;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;
import woowa.frame.web.parser.FormParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Router
public class QnaRouter {

    private final QnaService qnaService;
    private final ReplyService replyService;
    private final FormParser parser;

    public QnaRouter(QnaService qnaService, ReplyService replyService, FormParser parser) {
        this.qnaService = qnaService;
        this.replyService = replyService;
        this.parser = parser;
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

    /**
     * 질문(게시글)을 생성하는 요청을 처리합니다.
     * 게시글 작성은 로그인한 유저만이 실행이 가능합니다. 로그인 여부는 {@link woowa.cafe.filter.AuthFilter} 필터에서 확인합니다.
     * <li> POST /question </li>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return 생성된 질문의 상세 페이지로 리다이렉트합니다. 질문 생성에 실패하면 질문 작성 페이지로 리다이렉트합니다.
     * @see woowa.cafe.filter.AuthFilter 로그인 여부를 확인하는 필터
     */
    @HttpMapping(method = "POST", urlTemplate = "/question")
    public String createQuestion(HttpServletRequest request, HttpServletResponse response) {

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        CreateQuestionRequest req = new CreateQuestionRequest(
                userInfo.name(),
                request.getParameter("title"),
                request.getParameter("contents"),
                userInfo.id()
        );

        try {
            qnaService.createQna(req);
            return "redirect:/";
        } catch (RuntimeException ex) {
            return "redirect:/question";
        }
    }

    @HttpMapping(method = "PATCH", urlTemplate = "/question/{id}")
    public Integer updateQuestion(HttpServletRequest request, HttpServletResponse response) {

        String body = readBody(request);
        Map<String, String> params = parser.parse(body);

        String id = request.getRequestURI().substring(10);
        String title = params.get("title");
        String content = params.get("contents");
        String userId = (String) request.getSession().getAttribute("userId");

        UpdateQuestionRequest updateRequest = new UpdateQuestionRequest(
                id,
                userId,
                title,
                content
        );

        QuestionInfo question = qnaService.updateQuestion(updateRequest);

        if (question == null) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }

        request.setAttribute("question", question);
        return HttpServletResponse.SC_OK;
    }

    @HttpMapping(method = "DELETE", urlTemplate = "/question/{id}")
    public Integer deleteQuestion(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getRequestURI().substring(10);
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        if (!qnaService.deleteQuestion(id, userInfo.id())) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }

        return HttpServletResponse.SC_OK;
    }

    @HttpMapping(method = "GET", urlTemplate = "/question/{id}")
    public String getQuestion(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getRequestURI().substring(10);
        QuestionInfo question = qnaService.getQuestion(id);
        List<ReplyInfo> replies = replyService.getAllReplies(question.id());

        if (question == null) {
            return "redirect:/question";
        }

        request.setAttribute("question", question);
        request.setAttribute("replies", replies);
        return "/template/qna/detail.jsp";
    }

    @HttpMapping(method = "GET", urlTemplate = "/question/{id}/form")
    public String getQuestionEditForm(HttpServletRequest request, HttpServletResponse response) {
        return "/template/qna/edit.jsp";
    }

    private String readBody(HttpServletRequest request) {
        String body;
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try (InputStream input = request.getInputStream()) {
            body = URLDecoder.decode(new String(input.readAllBytes()), "UTF-8");
            return body;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
