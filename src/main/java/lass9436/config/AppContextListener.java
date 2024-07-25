package lass9436.config;

import lass9436.question.model.QuestionRepository;
import lass9436.question.model.QuestionRepositoryImpl;
import lass9436.user.model.UserRepository;
import lass9436.user.model.UserRepositoryImpl;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// UserRepository 인스턴스를 생성하고 컨텍스트에 저장
		UserRepository userRepository = new UserRepositoryImpl();
		sce.getServletContext().setAttribute("userRepository", userRepository);
		// QuestionRepository 인스턴스를 생성하고 컨텍스트에 저장
		QuestionRepository questionRepository = new QuestionRepositoryImpl();
		sce.getServletContext().setAttribute("questionRepository", questionRepository);
	}
}
