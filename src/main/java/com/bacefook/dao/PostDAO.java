package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostDAO {

	private static final int CONFIRMED = 1;

	private static final String GET_ALL_FRIENDS_POSTS_BY_DATE = "SELECT post.id AS post FROM "
			+ "((SELECT p.id,p.posting_time FROM relations r JOIN posts p ON(r.receiver_id=p.poster_id) WHERE r.sender_id=? AND r.is_confirmed="
			+ CONFIRMED + ")"
			+ "UNION "
			+ "(SELECT p.id,p.posting_time FROM relations r JOIN posts p ON(r.sender_id=p.poster_id) WHERE r.receiver_id=? AND r.is_confirmed="
			+ CONFIRMED + ")) " + "post " + "ORDER BY post.posting_time DESC;";
	
	private static final String DELETE_POST_BY_ID = "DELETE FROM posts WHERE id = ?;";
	private static final String UNLIKE_POST = "DELETE FROM post_likes WHERE post_id= ? AND user_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> getAllPostsIdByFriends(int id) {
		return jdbcTemplate.query(GET_ALL_FRIENDS_POSTS_BY_DATE, ps -> {
			ps.setInt(1, id);
			ps.setInt(2, id);
		}, (resultSet, rowNum) -> resultSet.getInt("post"));
	}
	
	public int deletePostById(int id) {
		return jdbcTemplate.update(DELETE_POST_BY_ID, id);
	}
	public int unlikePost(Integer postId,Integer userId) {
		return jdbcTemplate.update(UNLIKE_POST, postId, userId);
	}

}
