package com.bacefook.controller;

import java.security.NoSuchAlgorithmException;

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
import com.bacefook.security.Cryptography;
import com.bacefook.service.GenderService;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@RestController
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private GenderService genderService;

	@PostMapping("signup")
	public int signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request)
			throws InvalidUserCredentialsException, GenderNotFoundException, NoSuchAlgorithmException {

		new UserValidation().validate(signUp);

		Integer genderId = genderService.findByGenderName(signUp.getGender()).getId();
		String encodedPassword = Cryptography.cryptSHA256(signUp.getPassword());

		User user = new User(genderId, signUp.getEmail(), signUp.getFirstName(), signUp.getLastName(), encodedPassword,
				signUp.getBirthday());

		SessionManager.signInUser(request, user);
		return userService.saveUser(user);
	}

	@PostMapping("login")
	public int login(@RequestBody LoginDTO login, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException {
		UserValidation validation = new UserValidation();
		validation.validate(login);
		try {
			User user = userService.findUserByEmail(login.getEmail());
			user.getPassword().matches(Cryptography.cryptSHA256(login.getPassword()));

			SessionManager.signInUser(request, user);
			return user.getId();
		} catch (UserNotFoundException e) {
			throw new InvalidUserCredentialsException("Credentials do not match!");
		}
	}
}
