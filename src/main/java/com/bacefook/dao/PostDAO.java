package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostDAO {

	private static final String GET_ALL_FRIENDS_POSTS_BY_DATE = "SELECT p.id AS post FROM relations r "
			+ "LEFT JOIN posts p ON(r.receiver_id = p.poster_id) "
			+ "WHERE (r.sender_id =? OR r.receiver_id=?) AND r.is_confirmed = 1 " + "ORDER BY p.posting_time DESC;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> getAllPostsIdByFriends(int id) {
		return jdbcTemplate.query(GET_ALL_FRIENDS_POSTS_BY_DATE, ps -> {
			ps.setInt(1, id);
			ps.setInt(2, id);
		}, (resultSet, rowNum) -> resultSet.getInt("post"));
	}

}
