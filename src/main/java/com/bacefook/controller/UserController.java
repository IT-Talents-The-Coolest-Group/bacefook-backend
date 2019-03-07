package com.bacefook.controller;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

//@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private GenderService genderService;

	private ModelMapper mapper = new ModelMapper();

	@GetMapping("/")

	public void startingPage(HttpServletResponse response) throws IOException {
		response.sendRedirect(
				"https://bacefookcommunity.postman.co/collections/6778985-5d93e005-f4f3-49fb-bc1d-956ce5e813fa?workspace=1bf0d2e0-4007-4e35-8219-38fec2a53b9d&fbclid=IwAR3xRCIhE9w4EKR-YEC2Hvt111icy21wEpQ5JgQgvjzVguz_OIfLa2i8fJs");
	}

	@PostMapping("/users/changepassword")
	public ResponseEntity<Object> changeUserPassword(@RequestBody ChangePasswordDTO passDto, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, UnauthorizedException,
			ElementNotFoundException {

		UserValidation.validate(passDto);
		int userId = SessionManager.getLoggedUser(request);
		User user = userService.findById(userId);
		String oldPass = Cryptography.cryptSHA256(passDto.getOldPassword());
		if (user.getPassword().equals(oldPass)) {
			user.setPassword(Cryptography.cryptSHA256(passDto.getNewPassword()));
			userService.save(user);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new InvalidUserCredentialsException("Wrong password!");
		}
	}

	@PostMapping("/signup")
	public int signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request,
			HttpServletResponse response) throws InvalidUserCredentialsException, ElementNotFoundException,
			NoSuchAlgorithmException, UnauthorizedException, IOException {

		UserValidation.validate(signUp);
		if (SessionManager.isLogged(request)) {
			throw new UnauthorizedException("Please log out before you can register!");
		}

		if (userService.emailIsTaken(signUp.getEmail())) {
			throw new InvalidUserCredentialsException("That email is already taken!");
		}

		User user = new User();
		this.mapper.map(signUp, user);
		user.setPassword(Cryptography.cryptSHA256(signUp.getPassword()));
		user.setGenderId(genderService.findByName(signUp.getGender()).getId());

		userService.save(user);
		SessionManager.signInUser(request, user);

		return user.getId();
	}

	@PostMapping("/login")
	public int login(@RequestBody LoginDTO login, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, ElementNotFoundException,
			UnauthorizedException {
		if (!SessionManager.isLogged(request)) {
			UserValidation.validate(login);
			User user = userService.findByEmail(login.getEmail());

			if (user.getPassword().equals(Cryptography.cryptSHA256(login.getPassword()))) {
				SessionManager.signInUser(request, user);
				return user.getId();
			} else {
				throw new InvalidUserCredentialsException("Wrong login credentials!");
			}
		} else {
			throw new UnauthorizedException("Log out first before you log in!");
		}
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) throws UnauthorizedException {
		if (SessionManager.isLogged(request)) {
			String message = SessionManager.logOutUser(request);
			return message;
		} else {
			throw new UnauthorizedException("You are not logged in!");
		}
	}

}
