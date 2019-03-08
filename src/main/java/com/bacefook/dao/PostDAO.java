package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostDAO {

	private static final String GET_ALL_FRIENDS_POSTS_BY_DATE = "SELECT post.id AS post FROM "
			+ "(SELECT p.id,p.posting_time FROM relations r JOIN posts p ON(r.receiver_id=p.poster_id) WHERE r.sender_id=? AND r.is_confirmed=1 " 
			+ "UNION "
			+ "SELECT p.id,p.posting_time FROM relations r JOIN posts p ON(r.sender_id=p.poster_id) WHERE r.receiver_id=? AND r.is_confirmed=1) "
			+ "post "
			+ "ORDER BY post.posting_time;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> getAllPostsIdByFriends(int id) {
		return jdbcTemplate.query(GET_ALL_FRIENDS_POSTS_BY_DATE, ps -> {
			ps.setInt(1, id);
			ps.setInt(2, id);
		}, (resultSet, rowNum) -> resultSet.getInt("post"));
	}

}
