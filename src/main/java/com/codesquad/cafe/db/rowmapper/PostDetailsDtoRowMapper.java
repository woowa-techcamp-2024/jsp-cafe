package com.codesquad.cafe.db.rowmapper;

import com.codesquad.cafe.db.entity.PostDetailsDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostDetailsDtoRowMapper implements RowMapper<PostDetailsDto> {
    @Override
    public PostDetailsDto mapRow(ResultSet rs) throws SQLException {
        return new PostDetailsDto(
                rs.getLong("p_id"),
                rs.getString("p_title"),
                rs.getString("p_content"),
                rs.getString("p_filename"),
                rs.getInt("p_view"),
                rs.getLong("p_author_id"),
                rs.getString("u_username"),
                rs.getTimestamp("p_created_at").toLocalDateTime(),
                rs.getTimestamp("p_updated_at").toLocalDateTime(),
                rs.getBoolean("p_deleted")
        );
    }
}
