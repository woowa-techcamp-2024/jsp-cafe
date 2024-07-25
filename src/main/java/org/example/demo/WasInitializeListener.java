package org.example.demo;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.demo.db.DbConfig;
import org.example.demo.repository.PostRepository;
import org.example.demo.repository.UserRepository;

@WebListener
public class WasInitializeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DbConfig dbConfig = new DbConfig("jdbc:mysql://localhost/test", "root", "");
        UserRepository.init(dbConfig);
        PostRepository.init(dbConfig, UserRepository.getInstance());

        System.out.println("WasInitializeListener.contextInitialized finish!");
    }



}
