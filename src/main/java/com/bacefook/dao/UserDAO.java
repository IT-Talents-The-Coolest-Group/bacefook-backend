package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String FIND_ALL_FRIENDS_OF = 
			"SELECT r.receiver_id from relations r "
			+ "WHERE r.sender_id = ? and r.is_confirmed = 0"
			+ "UNION SELECT r.sender_id from relations r "
			+ "WHERE r.receiver_id = ? and r.is_confirmed = 0";
	
	
	private static final String GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS = "SELECT DISTINCT "
			+ "u.id AS id,r.is_confirmed FROM users u "
			+ "LEFT JOIN relations r ON ((r.receiver_id=u.id AND r.sender_id = ?)"
			+ "OR(r.sender_id = u.id AND r.receiver_id=?))"
			+ "WHERE CONCAT(u.first_name,' ',u.last_name) LIKE ? ORDER BY r.is_confirmed=1 DESC LIMIT 5;";

	public List<Integer> findAllRequestsTo(Integer userId) {
		return jdbcTemplate.query(FIND_ALL_FRIENDS_OF, 
			ps -> { 
				ps.setInt(1, userId);
				ps.setInt(2, userId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}
	
	

	public List<Integer> getAllSearchingMatchesOrderedByIfFriend(int userId,String search) {
		return jdbcTemplate.query(GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setString(3,"'" +search+"%'");
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}

}
