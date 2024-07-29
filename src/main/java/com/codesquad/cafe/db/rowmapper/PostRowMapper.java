package com.codesquad.cafe.db.rowmapper;

import com.codesquad.cafe.db.entity.Post;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs) throws SQLException {
            return new Post(
                    rs.getLong("id"),
                    rs.getLong("author_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("filename"),
                    rs.getInt("view"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getBoolean("deleted"));
    }
}