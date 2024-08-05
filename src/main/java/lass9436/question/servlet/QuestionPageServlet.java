package lass9436.question.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.comment.model.Comment;
import lass9436.comment.model.CommentRepository;
import lass9436.question.model.Question;
import lass9436.question.model.QuestionRepository;

@WebServlet("/questionPage")
public class QuestionPageServlet extends HttpServlet {

	private QuestionRepository questionRepository;
	private CommentRepository commentRepository;
	private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> actionMethodMap;
	private final String path = "/WEB-INF/question";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionRepository = (QuestionRepository)config.getServletContext().getAttribute("questionRepository");
		commentRepository = (CommentRepository)config.getServletContext().getAttribute("commentRepository");
		actionMethodMap = Map.of(
			"register", this::handleRegister,
			"list", this::handleList,
			"detail", this::handleDetail,
			"update", this::handleUpdate
		);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String action = req.getParameter("action");
			BiFunction<HttpServletRequest, HttpServletResponse, String> actionMethod = getHandler(action);
			String viewPath = actionMethod.apply(req, resp);
			req.getRequestDispatcher(path + viewPath).forward(req, resp);
		} catch (RuntimeException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		}
	}

	private BiFunction<HttpServletRequest, HttpServletResponse, String> getHandler(String action) {
		if (action == null || action.isEmpty())
			throw new IllegalArgumentException("action is null or empty");
		if (!actionMethodMap.containsKey(action))
			throw new IllegalArgumentException("unknown action: " + action);
		return actionMethodMap.get(action);
	}

	private String handleRegister(HttpServletRequest req, HttpServletResponse resp) {
		return "/register.jsp";
	}

	private String handleList(HttpServletRequest req, HttpServletResponse resp) {
		long page = getPageParameter(req.getParameter("page"));
		long pageSize = 15;
		req.setAttribute("questions", questionRepository.findAllPageable(page, pageSize));
		req.setAttribute("maxPage", (long) Math.ceil((double) questionRepository.count() / pageSize));
		return "/list.jsp";
	}

	private long getPageParameter(String pageParam) {
		if (pageParam == null || pageParam.trim().isEmpty()) {
			return 1;
		}
		try {
			long page = Long.parseLong(pageParam);
			return page > 0 ? page : 1;
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	private String handleDetail(HttpServletRequest req, HttpServletResponse resp) {
		long seq = Long.parseLong(req.getParameter("seq"));
		Question question = questionRepository.findByQuestionSeq(seq);
		List<Comment> comments = commentRepository.findByQuestionSeq(seq);
		req.setAttribute("question", question);
		req.setAttribute("comments", comments);
		return "/detail.jsp";
	}

	private String handleUpdate(HttpServletRequest req, HttpServletResponse resp) {
		long seq = Long.parseLong(req.getParameter("seq"));
		Question question = questionRepository.findByQuestionSeq(seq);
		long userSeq = (long)req.getSession().getAttribute("userSeq");
		if (question.getUserSeq() != userSeq) {
			throw new IllegalArgumentException("you are not allowed to update this question");
		}
		req.setAttribute("question", question);
		return "/update.jsp";
	}

}
