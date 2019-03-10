package com.bacefook.controller;

import java.util.List;

import javax.management.relation.RelationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.service.UserService;

@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@RestController
public class RelationsController {
	@Autowired
	private UserService userService;

	@GetMapping("{id}/friends")
	public List<UserSummaryDTO> getFriendsOfUser(@PathVariable Integer id) throws ElementNotFoundException {
		// TODO validate
		return userService.findAllFriendOf(id);
	}

	@PostMapping("{id}/friendrequest")
	public String sendFriendRequest(@PathVariable Integer id, HttpServletRequest request)
			throws RelationException, UnauthorizedException, ElementNotFoundException {

		userService.sendFriendRequest(SessionManager.getLoggedUser(request), id);
		return "Friend request was send to " + userService.findById(id).getFullName();
	}

	@GetMapping("/friendrequests")
	public List<UserSummaryDTO> getAllFriendRequests(HttpServletRequest request) throws UnauthorizedException {

		Integer receiverId = SessionManager.getLoggedUser(request);
		return userService.findAllFromRequestsTo(receiverId);
	}

	@PutMapping("{senderId}/acceptrequest")
	public String acceptFriendRequest(@PathVariable Integer senderId, HttpServletRequest request)
			throws UnauthorizedException, RelationException, ElementNotFoundException {
		// TODO validate if there is friend request, if you are not already friends
		Integer receiverId = SessionManager.getLoggedUser(request);
		userService.confirmFriendRequest(receiverId, senderId);
		return "You are now friends with " + userService.findById(senderId).getFullName();
	}
	
	@DeleteMapping("/friends")
	public String removeFromFriends(@RequestParam Integer friendId,HttpServletRequest request) throws UnauthorizedException {
		int loggedId = SessionManager.getLoggedUser(request);
		return userService.removeFromFriends(loggedId, friendId);
	}
	@DeleteMapping("/cancelrequests")
	public String cancelFriendRequest(@RequestParam Integer receiverId,HttpServletRequest request) throws UnauthorizedException {
		int loggedId = SessionManager.getLoggedUser(request);
		return userService.cancelFriendRequest(loggedId, receiverId);
	}
	@DeleteMapping("/deleterequests")
	public String deleteFriendRequest(@RequestParam Integer senderId,HttpServletRequest request) throws UnauthorizedException{
		int loggedId = SessionManager.getLoggedUser(request);
		return userService.deleteFriendRequest(loggedId, senderId);
	}
}
