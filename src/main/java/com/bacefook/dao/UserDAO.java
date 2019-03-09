package com.bacefook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

	private static final int LIMIT = 10;
	private static final int NOTCONFIRMED = 0;
	private static final int CONFIRMED = 1;

	private static final String FIND_ALL_FRIEND_REQUESTS_OF = "SELECT r.sender_id AS id FROM relations r WHERE r.receiver_id = ? AND r.is_confirmed="
			+ NOTCONFIRMED + " ORDER BY r.id DESC;";

	private static final String FIND_ALL_FRIENDS_OF = "SELECT id FROM (SELECT DISTINCT sender_id, receiver_id FROM relations "
			+ "WHERE (sender_id = ? OR receiver_id = ?) AND is_confirmed = " + CONFIRMED + ") r "
			+ "JOIN users u ON (u.id = r.sender_id OR u.id = r.receiver_id) WHERE id <> ?;";

	private static final String GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS = "SELECT DISTINCT "
			+ "u.id AS userId,r.is_confirmed FROM users u "
			+ "LEFT JOIN relations r ON ((r.receiver_id=u.id AND r.sender_id = ?) "
			+ "OR(r.sender_id = u.id AND r.receiver_id=?)) " + "WHERE CONCAT(u.first_name,' ',u.last_name) LIKE ? "
			+ "ORDER BY r.is_confirmed DESC LIMIT " + LIMIT + ";";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Integer> getAllSearchingMatchesOrderedByIfFriend(int userId, String search) {
		return jdbcTemplate.query(GET_USERS_BY_STRING_ORDER_BY_YOUR_FRIENDS, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setString(3, search + "%");
		}, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				rowNum = rs.getInt("userId");
				System.out.println(rowNum);
				return rowNum;
			}
		});
	}
	public List<Integer> findAllRequestsTo(Integer userId) {
		return jdbcTemplate.query(FIND_ALL_FRIEND_REQUESTS_OF, ps -> {
			ps.setInt(1, userId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}

	public List<Integer> findAllFriendsOf(Integer userId) {
		return jdbcTemplate.query(FIND_ALL_FRIENDS_OF, ps -> {
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt(3, userId);
		}, (resultSet, rowNum) -> resultSet.getInt("id"));
	}

}
