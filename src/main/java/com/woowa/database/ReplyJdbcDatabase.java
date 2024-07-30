package com.woowa.database;

import com.woowa.model.Reply;
import java.util.List;

public class ReplyJdbcDatabase implements ReplyDatabase {
    @Override
    public void save(Reply reply) {

    }

    @Override
    public List<Reply> findAll() {
        return List.of();
    }
}
