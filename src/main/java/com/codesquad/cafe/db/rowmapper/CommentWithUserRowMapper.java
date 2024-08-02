package com.codesquad.cafe.db.rowmapper;

import com.codesquad.cafe.db.domain.CommentWithUser;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentWithUserRowMapper implements RowMapper<CommentWithUser> {

    @Override
    public CommentWithUser mapRow(ResultSet rs) throws SQLException {
        return new CommentWithUser(
                rs.getLong("c_id"),
                rs.getLong("c_post_id"),
                rs.getLong("c_parent_id"),
                rs.getLong("c_user_id"),
                rs.getString("u_username"),
                rs.getString("c_content"),
                rs.getTimestamp("c_created_at").toLocalDateTime(),
                rs.getTimestamp("c_updated_at").toLocalDateTime(),
                rs.getBoolean("c_deleted"));
    }

}
