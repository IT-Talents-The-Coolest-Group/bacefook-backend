package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Integer> getAllPostsIdByFriends(int id){
		final String SQL = "SELECT r.receiver_id AS user,p.id AS post FROM relations r RIGHT JOIN posts p ON(r.receiver_id=p.poster_id) WHERE r.sender_id =? AND r.is_confirmed=1 ORDER BY";
		return jdbcTemplate.query(SQL, (resultSet,rowNum)->resultSet.getInt("post_id"));
	}
	
}
