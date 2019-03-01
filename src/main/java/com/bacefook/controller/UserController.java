package com.bacefook.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.FriendsListDTO;
import com.bacefook.exceptions.UserNotFoundException;
import com.bacefook.service.UserService;

@RestController
public class UserController {
	
	@Autowired private UserService userService;
	
		@GetMapping("{id}/friends")
		public Set<FriendsListDTO> getFriendsOfUser(@PathVariable Integer id) 
				throws UserNotFoundException {
			return userService
					.findUserById(id).getFriends()
					.stream().map(user -> 
					new FriendsListDTO(
						user.getId(), 
						user.getFirstName(), 
						user.getLastName(), 
						user.getFriends().size()))
					.collect(Collectors.toSet());
		}
	
	// TODO create a user/sign up
	
	// TODO send a friend request to a user
	// should create a new relation with the two users
	
	// TODO accept a friend request of a user
	// should change the relation column 'is_confirmed' to 1

	// TODO get all posts of a specific user
	
}
