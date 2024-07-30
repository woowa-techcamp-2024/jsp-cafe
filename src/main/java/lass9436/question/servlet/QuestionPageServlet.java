package lass9436.question.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiFunction;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.question.model.Question;
import lass9436.question.model.QuestionRepository;

@WebServlet("/questionPage")
public class QuestionPageServlet extends HttpServlet {

	private QuestionRepository questionRepository;
	private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> actionMethodMap;
	private final String path = "/WEB-INF/question";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionRepository = (QuestionRepository) config.getServletContext().getAttribute("questionRepository");
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
		if (action == null || action.isEmpty()) throw new IllegalArgumentException("action is null or empty");
		if (!actionMethodMap.containsKey(action)) throw new IllegalArgumentException("unknown action: " + action);
		return actionMethodMap.get(action);
	}

	private String handleRegister(HttpServletRequest req, HttpServletResponse resp) {
		return "/register.jsp";
	}

	private String handleList(HttpServletRequest req, HttpServletResponse resp) {
		req.setAttribute("questions", questionRepository.findAll());
		return "/list.jsp";
	}

	private String handleDetail(HttpServletRequest req, HttpServletResponse resp) {
		long seq = Long.parseLong(req.getParameter("seq"));
		Question question = questionRepository.findByQuestionSeq(seq);
		req.setAttribute("question", question);
		return "/detail.jsp";
	}

	private String handleUpdate(HttpServletRequest req, HttpServletResponse resp) {
		long seq = Long.parseLong(req.getParameter("seq"));
		// seq에 해당하는 질문을 가져와서 request attribute에 설정하는 로직 추가
		return "/update.jsp";
	}

}
