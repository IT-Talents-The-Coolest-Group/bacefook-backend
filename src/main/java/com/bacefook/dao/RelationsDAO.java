package com.bacefook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RelationsDAO {
	private static final String REMOVE_FROM_FRIENDS = "DELETE FROM relations WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id=? AND receiver_id=?)) AND is_confirmed = 1;";
	private static final String CANCEL_FRIEND_REQUEST = "DELETE FROM relations WHERE (sender_id=? AND receiver_id = ?) AND is_confirmed = 0;";
	private static final String DELETE_FRIEND_REQUEST = "DELETE FROM relations WHERE (sender_id=? AND receiver_id=?) AND is_confirmed = 0;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int removeFromFriends(Integer loggedId, Integer friendId) {
		return jdbcTemplate.update(REMOVE_FROM_FRIENDS, loggedId, friendId,friendId,loggedId);
	}
	
	public int cancelFriendRequest(Integer loggedId,Integer receiverId) {
		return jdbcTemplate.update(CANCEL_FRIEND_REQUEST,loggedId,receiverId);
	}
	
	public int deleteFriendRequest(Integer loggedId,Integer senderId) {
		return jdbcTemplate.update(DELETE_FRIEND_REQUEST,senderId,loggedId);
	}
	
}
