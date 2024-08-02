package lass9436.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lass9436.comment.model.CommentRepository;
import lass9436.comment.model.CommentRepositoryDBImpl;
import lass9436.question.model.QuestionRepository;
import lass9436.question.model.QuestionRepositoryDBImpl;
import lass9436.user.model.UserRepository;
import lass9436.user.model.UserRepositoryDBImpl;

@WebListener
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// UserRepository 인스턴스를 생성하고 컨텍스트에 저장
		//UserRepository userRepository = new UserRepositoryImpl();
		UserRepository userRepository = new UserRepositoryDBImpl();
		sce.getServletContext().setAttribute("userRepository", userRepository);
		// QuestionRepository 인스턴스를 생성하고 컨텍스트에 저장
		//QuestionRepository questionRepository = new QuestionRepositoryImpl();
		QuestionRepository questionRepository = new QuestionRepositoryDBImpl();
		sce.getServletContext().setAttribute("questionRepository", questionRepository);
		// CommentRepository 인스턴스를 생성하고 컨텍스트에 저장
		//CommentRepository commentRepository = new CommentRepositoryImpl();
		CommentRepository commentRepository = new CommentRepositoryDBImpl();
		sce.getServletContext().setAttribute("commentRepository", commentRepository);
	}
}
