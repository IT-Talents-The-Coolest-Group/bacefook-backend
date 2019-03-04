package com.bacefook.controller;

import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RelationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.FriendsListDTO;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.User;
import com.bacefook.service.UserService;

@RestController
public class RelationsController {
	@Autowired
	private UserService userService;
	
	@GetMapping("{id}/friends")
	public Set<FriendsListDTO> getFriendsOfUser(@PathVariable Integer id) throws UserNotFoundException {
		return userService.findUserById(id).getFriends().stream().map(
				user -> new FriendsListDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getFriends().size()))
				.collect(Collectors.toSet());
	}
	
	@PutMapping("{id}/friendrequest")
	public void sendFriendRequest(@PathVariable Integer id, HttpServletRequest request) 
			throws RelationException, UnauthorizedException, UserNotFoundException {
		
		if (SessionManager.isLogged(request)) {
			User user = (User) request.getSession().getAttribute("logged");
			userService.makeRelation(user.getId(), id);
		}
		else {
			throw new UnauthorizedException("You are not logged in!");
		}
		
	}

	// TODO accept a friend request of a user
	// should change the relation column 'is_confirmed' to 1
}
