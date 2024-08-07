package com.woowa.config;

import com.woowa.database.question.QuestionDatabase;
import com.woowa.database.reply.ReplyDatabase;
import com.woowa.database.user.UserDatabase;
import com.woowa.framework.Bean;
import com.woowa.handler.ReplyHandler;
import com.woowa.handler.LoginHandler;
import com.woowa.handler.QuestionHandler;
import com.woowa.handler.UserHandler;

public class HandlerConfig {

    @Bean
    public UserHandler userHandler(UserDatabase userDatabase) {
        return new UserHandler(userDatabase);
    }

    @Bean
    public QuestionHandler questionHandler(UserDatabase userDatabase, QuestionDatabase questionDatabase, ReplyDatabase replyDatabase) {
        return new QuestionHandler(userDatabase, questionDatabase);
    }

    @Bean
    public LoginHandler loginHandler(UserDatabase userDatabase) {
        return new LoginHandler(userDatabase);
    }

    @Bean
    public ReplyHandler replyHandler(UserDatabase userDatabase, QuestionDatabase questionDatabase, ReplyDatabase replyDatabase) {
        return new ReplyHandler(userDatabase, questionDatabase, replyDatabase);
    }
}
