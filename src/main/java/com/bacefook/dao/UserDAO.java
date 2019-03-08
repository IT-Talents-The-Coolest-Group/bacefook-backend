package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String FIND_ALL_FRIENDS_OF = 
			"SELECT r.receiver_id from relations r "
			+ "WHERE r.sender_id = ? and r.is_confirmed = 0"
			+ "UNION SELECT r.sender_id from relations r "
			+ "WHERE r.receiver_id = ? and r.is_confirmed = 0";
	
	
	public List<Integer> findAllRequestsTo(Integer userId) {
		return jdbcTemplate.query(FIND_ALL_FRIENDS_OF, 
			ps -> { 
				ps.setInt(1, userId);
				ps.setInt(2, userId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}
	
	
	
}
