package com.bacefook.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RelationException;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<Set<UserSummaryDTO>> getFriendsOfUser(@PathVariable Integer id)
			throws ElementNotFoundException {
		
		return new ResponseEntity<Set<UserSummaryDTO>>(
				userService.
				findById(id)
				.getFriends()
				.stream()
				.map(user -> {
					UserSummaryDTO userDTO = null;
					new ModelMapper().map(user, userDTO);
					return userDTO;
				})
				.collect(Collectors.toSet()), HttpStatus.OK);
	}

	@PutMapping("{id}/friendrequest")
	public ResponseEntity<Object> sendFriendRequest(@PathVariable Integer id, HttpServletRequest request)
			throws RelationException, UnauthorizedException, ElementNotFoundException {

		userService.sendFriendRequest(SessionManager.getLoggedUser(request).getId(), id);
		return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("friendrequests")
	public ResponseEntity<List<UserSummaryDTO>> getAllFriendRequests(HttpServletRequest request) 
			throws UnauthorizedException {
		
		Integer receiverId = SessionManager.getLoggedUser(request).getId();
		
		return new ResponseEntity<List<UserSummaryDTO>>(
				userService
				.findAllFromRequestsTo(receiverId)
				.stream()
				.map(user -> {
					UserSummaryDTO friendDTO = null;
					new ModelMapper().map(user, friendDTO); 
					return friendDTO;
				})
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	// TODO add to postman
	@PutMapping("{senderId}/acceptrequest")
	public ResponseEntity<Object> acceptFriendRequest(@PathVariable Integer senderId, HttpServletRequest request) 
			throws UnauthorizedException {
		
		Integer receiverId = SessionManager.getLoggedUser(request).getId();
		userService.confirmFriendRequest(receiverId, senderId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
