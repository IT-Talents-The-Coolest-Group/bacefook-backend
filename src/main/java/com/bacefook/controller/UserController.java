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
				&& UserValidation.isValidBirthday(signUp.getBirthday())
				&& UserValidation.isValidGender(signUp.getGender())) {
			if (signUp.getFirstName().isEmpty()) {
				throw new InvalidUserException("Wrong sign up credentials!");
			}
			if (signUp.getLastName().isEmpty()) {
				throw new InvalidUserException("Wrong sign up credentials!");
			}
			User user = new User(1, email, signUp.getFirstName(), signUp.getLastName(), signUp.getPassword(),
					signUp.getBirthday());

			SessionManager.signInUser(request, user);// Session is set to logged
			System.out.println(user);

			return userService.saveUser(user);
		}
		throw new InvalidUserException("Wrong sign up credentials!");
	}

	// login
	@PostMapping("login")
	public int login(@RequestBody LoginDTO login, HttpServletRequest request) throws InvalidUserException {
		System.out.println(login);
		String email = login.getEmail();
		String password = login.getPassword();
		if (email.isEmpty()) {
			throw new InvalidUserException("Email must not be empty!");
		}
		User user = new UserService().findUserByEmail(login.getEmail());//null
		System.out.println(user);
		if (user == null) {
			throw new InvalidUserException("Wrong login credentials!");
		}
		if (password.isEmpty()) {
			throw new InvalidUserException("Password must not be empty!");
		}
//		if (!user.getPassword().equals(login.getPassword())) {
//			throw new InvalidUserException("Wrong login credentials!");
//		}
		SessionManager.signInUser(request, user);
		return user.getId();
	}

	// TODO get all posts of a specific user

};