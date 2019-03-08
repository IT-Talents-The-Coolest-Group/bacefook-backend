package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
	private static final String GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS = "SELECT DISTINCT "
			+ "u.id AS id,r.is_confirmed FROM users u "
			+ "LEFT JOIN relations r ON ((r.receiver_id=u.id AND r.sender_id = ?)"
			+ "OR(r.sender_id = u.id AND r.receiver_id=?))"
			+ "WHERE CONCAT(u.first_name,' ',u.last_name) LIKE ? ORDER BY r.is_confirmed=1 DESC LIMIT 5;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> getAllSearchingMatchesOrderedByIfFriend(int userId,String search) {
		return jdbcTemplate.query(GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setString(3,"'" +search+"%'");
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}

}
