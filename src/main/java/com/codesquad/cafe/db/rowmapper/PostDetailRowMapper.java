package com.codesquad.cafe.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.codesquad.cafe.model.aggregate.CommentWithUser;
import com.codesquad.cafe.model.aggregate.PostDetail;

public class PostDetailRowMapper implements ResultSetExtractor<PostDetail> {

	@Override
	public PostDetail extractData(ResultSet rs) throws SQLException {
		Map<Integer, PostDetail> postDetailMap = new HashMap<>();
		PostDetail postDetail = null;

		while (rs.next()) {
			int postId = rs.getInt("p_id");

			postDetail = postDetailMap.get(postId);
			if (postDetail == null) {
				postDetail = new PostDetail();
				postDetail.setPostId(rs.getLong("p_id"));
				postDetail.setTitle(rs.getString("p_title"));
				postDetail.setContent(rs.getString("p_content"));
				postDetail.setFileName(rs.getString("p_filename"));
				postDetail.setView(rs.getInt("p_view"));
				postDetail.setAuthorId(rs.getLong("p_author_id"));
				postDetail.setAuthorUsername(rs.getString("u_username"));
				postDetail.setCreatedAt(rs.getTimestamp("p_created_at").toLocalDateTime());
				postDetail.setUpdatedAt(rs.getTimestamp("p_updated_at").toLocalDateTime());
				postDetail.setDeleted(rs.getBoolean("p_deleted"));
				postDetailMap.put(postId, postDetail);
			}

			int commentId = rs.getInt("c_id");
			if (commentId > 0) {
				CommentWithUser comment = new CommentWithUser(
					rs.getLong("c_id"),
					rs.getLong("c_post_id"),
					rs.getLong("c_parent_id"),
					rs.getLong("c_user_id"),
					rs.getString("cu_username"),
					rs.getString("c_content"),
					rs.getTimestamp("c_created_at").toLocalDateTime(),
					rs.getTimestamp("c_updated_at").toLocalDateTime(),
					rs.getBoolean("c_deleted"));
				postDetail.addComment(comment);
			}
		}
		return postDetail;
	}

}
