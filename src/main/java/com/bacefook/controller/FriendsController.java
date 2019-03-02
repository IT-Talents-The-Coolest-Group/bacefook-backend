package com.bacefook.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.FriendsListDTO;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.User;
import com.bacefook.service.UserService;

@RestController
public class FriendsController {
	@Autowired
	private UserService userService;

	@GetMapping("{id}/friends")
	public Set<FriendsListDTO> getFriendsOfUser(@PathVariable Integer id) throws UserNotFoundException {
		return userService.findUserById(id).getFriends().stream().map(user -> new FriendsListDTO(user.getId(),
				user.getFirstName(), user.getLastName(), user.getFriends().size())).collect(Collectors.toSet());
	}
	// TODO send a friend request to a user
	// should create a new relation with the two users
	@PutMapping("{id}/friendrequest")
	public void sendFriendRequest(@RequestParam Integer id) {
		User u = new User();
		// userService.makeRelation(u, id); // TODO fix this
	}

	// TODO accept a friend request of a user
	// should change the relation column 'is_confirmed' to 1
}
