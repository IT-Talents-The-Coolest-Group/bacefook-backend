package com.bacefook.dao;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bacefook.model.Post;


@Component
public class PostDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	public List<Post> getAllPostsByFriends(int id){
//		final String SQL = "SELECT r.receiver_id AS user,p.id AS post FROM relations r RIGHT JOIN posts p ON(r.receiver_id=p.poster_id) WHERE r.sender_id =? AND r.is_confirmed=1";
//		jdbcTemplate.query(SQL, PostMapper pm = new PostMapper(){
//			
//		})
//	    jdbcTemplate.query(
//	         SQL, new PreparedStatementSetter() {
//			@Override
//			public void setValues(PreparedStatement preparedStatement) throws SQLException {
//				// TODO Auto-generated method stub
//	            preparedStatement.setInt(1, id);
//			}
//	      },
//	      new StudentMapper());
//	}
	
}
