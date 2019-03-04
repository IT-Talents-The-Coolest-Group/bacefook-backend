package com.bacefook.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.ChangePasswordDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.User;
import com.bacefook.security.Cryptography;
import com.bacefook.service.GenderService;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private GenderService genderService;

	@PostMapping("/users/changepassword")
	public void changeUserPassword(@RequestBody ChangePasswordDTO passDto, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, UnauthorizedException,
			ElementNotFoundException {
		new UserValidation().validate(passDto);
		int userId = SessionManager.getLoggedUser(request);
		if (userId != -1) {
			User user = userService.findUserById((Integer) userId);
			String oldPass = Cryptography.cryptSHA256(passDto.getOldPassword());
			if (user.getPassword().matches(oldPass)) {
				user.setPassword(Cryptography.cryptSHA256(passDto.getNewPassword()));
				userService.saveUser(user);
			} else {
				throw new InvalidUserCredentialsException("Wrong password!");
			}
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before changing your password.");
		}
	}

	@PostMapping("signup")
	public Integer signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request)
			throws InvalidUserCredentialsException, ElementNotFoundException, NoSuchAlgorithmException {

		new UserValidation().validate(signUp);

		Integer genderId = genderService.findByGenderName(signUp.getGender()).getId();

		if (userService.emailIsTaken(signUp.getEmail())) {
			throw new InvalidUserCredentialsException("That email is already taken!");
		}
		String encodedPassword = Cryptography.cryptSHA256(signUp.getPassword());

		User user = new User(genderId, signUp.getEmail(), signUp.getFirstName(), signUp.getLastName(), encodedPassword,
				signUp.getBirthday());

		SessionManager.signInUser(request, user.getId());
		return userService.saveUser(user);
	}

	@PostMapping("/login")
	public Integer login(@RequestBody LoginDTO login, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, ElementNotFoundException {

		new UserValidation().validate(login);
		User user = userService.findUserByEmail(login.getEmail());

		if (user.getPassword().matches(Cryptography.cryptSHA256(login.getPassword()))) {
//			response.setHeader("location", "https://google.bg");
//			HttpHeaders headers = new HttpHeaders();
			SessionManager.signInUser(request, user.getId());
			return user.getId();
		} else {
			throw new InvalidUserCredentialsException("Wrong login credentials!");
		}
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) throws UnauthorizedException {
		if (SessionManager.getLoggedUser(request) != -1) {
			String message = SessionManager.logOutUser(request);
			return message;
		} else {
			throw new UnauthorizedException("You are not logged in!");
		}
	}

}
