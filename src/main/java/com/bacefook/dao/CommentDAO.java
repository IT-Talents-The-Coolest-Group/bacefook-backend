package com.bacefook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addLikeToComment(Integer commentId, Integer userId) {
		jdbcTemplate.update("INSERT INTO comment_likes(comment_id,user_id) VALUES(?,?);", commentId, userId);
	}
}
