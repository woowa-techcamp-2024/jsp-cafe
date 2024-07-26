package lass9436;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.question.model.QuestionRepository;

@WebServlet("")
public class IndexServlet extends HttpServlet {

	private QuestionRepository questionRepository;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		questionRepository = (QuestionRepository)config.getServletContext().getAttribute("questionRepository");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("questions", questionRepository.findAll());
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
}
