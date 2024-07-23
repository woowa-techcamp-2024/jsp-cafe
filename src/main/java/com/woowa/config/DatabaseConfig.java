package com.woowa.config;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.framework.Bean;

public class DatabaseConfig {

    @Bean
    public UserDatabase userDatabase() {
        return new UserMemoryDatabase();
    }
}
