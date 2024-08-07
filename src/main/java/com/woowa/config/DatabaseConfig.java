package com.woowa.config;

import com.woowa.database.question.QuestionDatabase;
import com.woowa.database.question.QuestionJdbcDatabase;
import com.woowa.database.reply.ReplyDatabase;
import com.woowa.database.reply.ReplyJdbcDatabase;
import com.woowa.database.user.UserDatabase;
import com.woowa.database.user.UserJdbcDatabase;
import com.woowa.framework.Bean;

public class DatabaseConfig {

    @Bean
    public UserDatabase userDatabase() {
        return new UserJdbcDatabase();
    }

    @Bean
    public QuestionDatabase questionDatabase() {
        return new QuestionJdbcDatabase();
    }

    @Bean
    public ReplyDatabase replyDatabase() {
        return new ReplyJdbcDatabase();
    }
}
