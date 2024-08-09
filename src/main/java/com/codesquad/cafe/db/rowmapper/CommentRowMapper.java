package com.codesquad.cafe.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.codesquad.cafe.db.domain.Comment;

public class CommentRowMapper implements RowMapper<Comment> {

	@Override
	public Comment mapRow(ResultSet rs) throws SQLException {
		return new Comment(
			rs.getLong("id"),
			rs.getLong("post_id"),
			rs.getObject("parent_id", Long.class),
			rs.getLong("user_id"),
			rs.getString("content"),
			rs.getTimestamp("created_at").toLocalDateTime(),
			rs.getTimestamp("updated_at").toLocalDateTime(),
			rs.getBoolean("deleted"));
	}

}
