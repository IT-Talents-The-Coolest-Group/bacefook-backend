package com.bacefook.controller;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.ChangePasswordDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.dto.UserInfoDTO;
import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.AlreadyContainsException;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.User;
import com.bacefook.security.Cryptography;
import com.bacefook.service.UserService;
import com.bacefook.utility.UserValidation;

@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public void startingPage(HttpServletResponse response) throws IOException {
		response.sendRedirect(
				"https://bacefookcommunity.postman.co/collections/6778985-5d93e005-f4f3-49fb-bc1d-956ce5e813fa?workspace=1bf0d2e0-4007-4e35-8219-38fec2a53b9d&fbclid=IwAR3xRCIhE9w4EKR-YEC2Hvt111icy21wEpQ5JgQgvjzVguz_OIfLa2i8fJs");
	}

	@GetMapping("/users/search")
	public List<UserSummaryDTO> getAllUsersBySearch(@RequestParam String input,HttpServletRequest request) throws UnauthorizedException {
		Integer userId = SessionManager.getLoggedUser(request);
		List<UserSummaryDTO> users = userService.searchByNameOrderedAndLimited(input, userId);
		return users;
	}

	@PostMapping("/users/changepassword")
	public void changeUserPassword(@RequestBody ChangePasswordDTO passDto, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, UnauthorizedException,
			ElementNotFoundException {
		UserValidation.validate(passDto);
		int userId = SessionManager.getLoggedUser(request);
		userService.changePassword(userId, passDto.getOldPassword(), passDto.getNewPassword());
	}

	@PostMapping("/users/signup")
	public Integer signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request, HttpServletResponse response)
			throws InvalidUserCredentialsException, ElementNotFoundException, NoSuchAlgorithmException,
			UnauthorizedException, IOException {
		UserValidation.validate(signUp);
		if (SessionManager.isLogged(request)) {
			throw new UnauthorizedException("Please log out before you can register!");
		}

		if (userService.emailIsTaken(signUp.getEmail())) {
			throw new InvalidUserCredentialsException("That email is already taken!");
		}

		User user = userService.save(signUp);
		SessionManager.signInUser(request, user);

		return user.getId();
	}

	@PostMapping("/users/login")
	public Integer login(@RequestBody LoginDTO login, HttpServletRequest request)
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

	@PostMapping("/users/logout")
	public String logout(HttpServletRequest request) throws UnauthorizedException {
		if (SessionManager.isLogged(request)) {
			String message = SessionManager.logOutUser(request);
			return message;
		} else {
			throw new UnauthorizedException("You are not logged in!");
		}
	}
	
	@PostMapping("/users/setup")
	public UserInfoDTO setUpProfile(@RequestBody UserInfoDTO infoDto,HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException, AlreadyContainsException {
		int userId = SessionManager.getLoggedUser(request);
		if(userService.getInfoByPhone(infoDto.getPhone())!=null) {
			throw new AlreadyContainsException("A user with that phone already exists");
		}
		return userService.save(infoDto,userId);
	}

}
