package com.codesquad.cafe.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.codesquad.cafe.db.domain.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs) throws SQLException {
		return new User(
			rs.getLong("id"),
			rs.getString("username"),
			rs.getString("password"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getTimestamp("created_at").toLocalDateTime(),
			rs.getTimestamp("updated_at").toLocalDateTime(),
			rs.getBoolean("deleted")
		);
	}

}
