package com.bacefook.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RelationException;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.service.UserService;

@CrossOrigin
@RestController
public class RelationsController {
	@Autowired
	private UserService userService;

	@GetMapping("{id}/friends")
	public Set<UserSummaryDTO> getFriendsOfUser(@PathVariable Integer id)
			throws ElementNotFoundException {
		
		return 	userService.
				findById(id)
				.getFriends()
				.stream()
				.map(user -> {
					UserSummaryDTO userDTO = new UserSummaryDTO();
					new ModelMapper().map(user, userDTO);
					return userDTO;
				})
				.collect(Collectors.toSet());
	}

	@PostMapping("{id}/friendrequest")
	public String sendFriendRequest(@PathVariable Integer id, HttpServletRequest request)
			throws RelationException, UnauthorizedException, ElementNotFoundException {

		userService.sendFriendRequest(SessionManager.getLoggedUser(request), id);
		// TODO
		return "Friend request was send to "+id;//TODO get name by user id
	}

	@GetMapping("friendrequests")
	public List<UserSummaryDTO> getAllFriendRequests(HttpServletRequest request) 
			throws UnauthorizedException {
		
		Integer receiverId = SessionManager.getLoggedUser(request);
		
		return userService
				.findAllFromRequestsTo(receiverId)
				.stream()
				.map(user -> {
					UserSummaryDTO friendDTO = new UserSummaryDTO();
					new ModelMapper().map(user, friendDTO); 
					return friendDTO;
				})
				.collect(Collectors.toList());
	}

	// TODO add to postman
	@PutMapping("{senderId}/acceptrequest")
	public String acceptFriendRequest(@PathVariable Integer senderId, HttpServletRequest request) 
			throws UnauthorizedException {
		//TODO validate if there is friend request, if you are not already friends
		Integer receiverId = SessionManager.getLoggedUser(request);
		userService.confirmFriendRequest(receiverId, senderId);
		return "You are now friends with "+senderId;//TODO get name by id
	}
}
