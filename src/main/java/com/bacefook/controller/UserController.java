package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.GenderNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.User;
import com.bacefook.service.GenderService;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@RestController
public class UserController {

	@Autowired private UserService userService;
	@Autowired private GenderService genderService;

	@PostMapping("signup")
	public Integer signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request) 
			throws InvalidUserCredentialsException, GenderNotFoundException {

		new UserValidation().validate(signUp);

		Integer genderId = genderService.findByGenderName(signUp.getGender()).getId();

		User user = new User(genderId, signUp.getEmail(), signUp.getFirstName(), 
				signUp.getLastName(), signUp.getPassword(), signUp.getBirthday());

		SessionManager.signInUser(request, user);

		return userService.saveUser(user);
	}

	@PostMapping("login")
	public int login(@RequestBody LoginDTO login, HttpServletRequest request) throws InvalidUserCredentialsException {
		
		UserValidation validation = new UserValidation();
		validation.validate(login);
		User user = null;
		try {
			user = userService.findUserByEmail(login.getEmail());
		}
		catch (UserNotFoundException e) {
			throw new InvalidUserCredentialsException("Credentials do not match!");
		}
		String encryptedPassword = login.getPassword(); // TODO do encryption here
		
		validation.confirmPassword(encryptedPassword, user.getPassword());
		SessionManager.signInUser(request, user);
		return user.getId();
	}

	// TODO get all posts of a specific user

};