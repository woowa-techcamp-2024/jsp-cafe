package camp.woowa.jspcafe.servlet.view;

import camp.woowa.jspcafe.core.ServiceLocator;
import camp.woowa.jspcafe.db.page.Page;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.service.QuestionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "")
public class WelcomeViewServlet extends HttpServlet {
    private final QuestionService questionService;
    private final int PAGE_SIZE = 15;
    private final int PAGE_BLOCK_SIZE = 5;

    public WelcomeViewServlet() {
        questionService = ServiceLocator.getService(QuestionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (("".equalsIgnoreCase(pathInfo) || "/".equalsIgnoreCase(pathInfo))) {
            try {
                String page = req.getParameter("p");
                if (page == null) { // page 가 null 일 경우 1로 초기화
                    page = "1";
                }
                Integer p = Integer.parseInt(page);
                if (p < 1) { // p가 1보다 작을 경우 1로 초기화
                    p = 1;
                }

                Page<Question> questionPage = questionService.findAllWithPage(new PageRequest(p, PAGE_SIZE));
                int startPage = (questionPage.getCurrentPage() / PAGE_SIZE) * PAGE_SIZE + 1;
                req.setAttribute("questions", questionPage.getContents());
                req.setAttribute("startPage", startPage);
                req.setAttribute("endPage", Math.min((int) (Math.floor(questionPage.getTotalPage() / PAGE_SIZE) + 1), startPage + PAGE_BLOCK_SIZE));
                req.setAttribute("currentPage", questionPage.getCurrentPage());
                req.setAttribute("totalPage", (int) (Math.floor(questionPage.getTotalPage() / PAGE_SIZE) + 1));
                req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Error", e);
            }
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }
}
