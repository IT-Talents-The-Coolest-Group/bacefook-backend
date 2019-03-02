package com.bacefook.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.FriendsListDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.service.UserService;

@RestController
public class UserController {
	
	@Autowired private UserService userService;
	
		@GetMapping("{id}/friends")
		public Set<FriendsListDTO> getFriendsOfUser(@PathVariable Integer id) throws UserNotFoundException {
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
	@PostMapping("signup")
	public void signUp(@RequestBody SignUpDTO signUp) {
		// TODO implement validation for signing up
		// TODO maybe extract all the validation in their own methods in a new class
		if (signUp.getPassword().equals(signUp.getPasswordConfirmation())) {
			
		}
	}
	
	// TODO implement
	@PostMapping("login")
	public boolean login(@RequestBody LoginDTO login) {
		return false;
	}
	
	// TODO send a friend request to a user
	// should create a new relation with the two users
	
	// TODO accept a friend request of a user
	// should change the relation column 'is_confirmed' to 1

	// TODO get all posts of a specific user
	
}
