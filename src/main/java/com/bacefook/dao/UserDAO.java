package com.bacefook.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
public class UserDAO {

	private static final String INSERT_RELATION = "INSERT INTO relations values (0, ?, ?)";
	
	@Autowired
	@Setter private JdbcTemplate jdbcTemplate;

	public void createRelation(Integer senderId, Integer receiverId) {
		
		
		
		List<Integer> userIds = Arrays.asList(senderId, receiverId);
		jdbcTemplate.update(new PreparedStatementCreatorFactory(INSERT_RELATION).newPreparedStatementCreator(userIds));
	}

}
