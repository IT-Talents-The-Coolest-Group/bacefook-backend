package com.bacefook.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.InvalidUserException;
import com.bacefook.model.User;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	// create a user/sign up
	@PostMapping("signup")
	public Integer signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request) throws InvalidUserException {
		String email = signUp.getEmail();
		if (UserValidation.isValidEmail(email)
				&& UserValidation.isValidPassword(signUp.getPassword(), signUp.getPasswordConfirmation())
				&& UserValidation.isValidBirthday(signUp.getBirthday())&&UserValidation.isValidGender(signUp.getGender())) {
			if (signUp.getFirstName().isEmpty()) {
				throw new InvalidUserException("Wrong login credentials!");
			}
			if (signUp.getLastName().isEmpty()) {
				throw new InvalidUserException("Wrong login credentials!");
			}
			User user = new User(1, email, signUp.getFirstName(), signUp.getLastName(), signUp.getPassword(),
					signUp.getBirthday());

			SessionManager.signInUser(request, user);// Session is set to logged
			System.out.println(user);

			return userService.saveUser(user);
		}
		throw new InvalidUserException("Wrong login credentials!");
	}

	// login
	@PostMapping("login")
	public boolean login(@RequestBody LoginDTO login) {
		
		return false;
	}

	// TODO get all posts of a specific user

};