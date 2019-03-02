package com.bacefook.controller;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.FriendsListDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.InvalidUserException;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.User;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("{id}/friends")
	public Set<FriendsListDTO> getFriendsOfUser(@PathVariable Integer id) throws UserNotFoundException {
		return userService.findUserById(id).getFriends().stream().map(user -> new FriendsListDTO(user.getId(),
				user.getFirstName(), user.getLastName(), user.getFriends().size())).collect(Collectors.toSet());
	}

	// TODO create a user/sign up
	@PostMapping("signup")
	public void signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request) throws InvalidUserException {
		String email = signUp.getEmail();
		if (UserValidation.isValidEmail(email)
				&& UserValidation.isValidPassword(signUp.getPassword(), signUp.getPasswordConfirmation())
				&& UserValidation.isValidBirthday(signUp.getBirthday())) {
			if (signUp.getFirstName().isEmpty()) {
				throw new InvalidUserException("Wrong login credentials!");
			}
			if (signUp.getLastName().isEmpty()) {
				throw new InvalidUserException("Wrong login credentials!");
			}
//			if(signUp.getGender().isEmpty())
			// TODO gender
			User user = new User(null, 1, email, signUp.getFirstName(), signUp.getLastName(), signUp.getPassword(),
					signUp.getBirthday());
			// TODO save to DB
			// TODO setID

			SessionManager.signInUser(request, user);// Session is set to logged
			System.out.println(user);
		}
	}

	// TODO implement
	@PostMapping("login")
	public boolean login(@RequestBody LoginDTO login) {
		return false;
	}

	// TODO send a friend request to a user
	// should create a new relation with the two users
	@PutMapping("{id}/friendrequest")
	public void sendFriendRequest(@RequestParam Integer id) {
		User u = new User();
		//userService.makeRelation(u, id); // TODO fix this
	}
	
	// TODO accept a friend request of a user
	// should change the relation column 'is_confirmed' to 1

	// TODO get all posts of a specific user

}
