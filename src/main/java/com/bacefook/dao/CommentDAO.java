package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentDAO {
	private static final String ADD_LIKE_TO_COMMENT = "INSERT INTO comment_likes(comment_id,user_id) VALUES(?,?);";
	private static final String DELETE_COMMENT_BY_ID = "DELETE FROM comments WHERE id = ?;";
	private static final String UNLIKE_COMMENT = "DELETE FROM comment_likes WHERE comment_id= ? AND user_id = ?;";
	private static final String FIND_COMMENT_LIKE_BY = "SELECT id FROM comment_likes WHERE user_id= ? AND comment_id= ?;";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addLikeToComment(Integer commentId, Integer userId) {
		jdbcTemplate.update(ADD_LIKE_TO_COMMENT, commentId, userId);
	}

	public int unlikeComment(Integer commentId,Integer userId) {
		return jdbcTemplate.update(UNLIKE_COMMENT, commentId, userId);
	}

	public int deleteCommentById(int id) {
		return jdbcTemplate.update(DELETE_COMMENT_BY_ID, id);
	}
	public List<Integer> findCommentLikeByUserIdAndCommentId(Integer userId, Integer commentId ) {
		return jdbcTemplate.query(FIND_COMMENT_LIKE_BY, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, commentId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}
}
