package com.woowa.config;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.framework.Bean;
import com.woowa.handler.QuestionHandler;
import com.woowa.handler.UserHandler;

public class HandlerConfig {

    @Bean
    public UserHandler userHandler(UserDatabase userDatabase) {
        return new UserHandler(userDatabase);
    }

    @Bean
    public QuestionHandler questionHandler(UserDatabase userDatabase, QuestionDatabase questionDatabase) {
        return new QuestionHandler(userDatabase, questionDatabase);
    }
}
