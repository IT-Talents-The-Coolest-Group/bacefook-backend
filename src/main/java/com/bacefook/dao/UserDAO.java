package com.bacefook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bacefook.model.User;

@Component
public class UserDAO {
	private static final String GET_ALL_USERS = "SELECT * FROM users;";
//	private List<User> users;

	private JdbcTemplate jdbcTemplate;

	public List<User> getAllUsers() throws SQLException {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery(GET_ALL_USERS);
		List<User> users = new LinkedList<User>();
		while (rs.next()) {
			// 1  2      3      4         5        6        7
			//id gender email firstName lastNAme password birthday  DB
			//id email password firstName lastNAme birthday gender  model
			users.add(new User(rs.getInt(1), rs.getString(3), rs.getString(6), rs.getString(4), rs.getString(5), rs.getDate(7).toLocalDate(), rs.getInt(2)));
		}
		System.out.println(users);
		return users;
	}
	
	
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
