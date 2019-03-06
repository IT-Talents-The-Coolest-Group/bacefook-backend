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

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private GenderService genderService;

	@GetMapping("/")
	public void startingPage(HttpServletResponse response) throws IOException {
		response.sendRedirect("https://github.com/IT-Talents-The-Coolest-Group/bacefook-backend/blob/master/README.md");
	}
	
	@GetMapping("/home")
	public void homePage(HttpServletRequest request) throws UnauthorizedException {
		User user = SessionManager.getLoggedUser(request);
	}

	@PostMapping("/users/changepassword")
	public void changeUserPassword(@RequestBody ChangePasswordDTO passDto, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, UnauthorizedException,
			ElementNotFoundException {

		new UserValidation().validate(passDto);
		User user = SessionManager.getLoggedUser(request);
//			User user = userService.findUserById((Integer) user);
		String oldPass = Cryptography.cryptSHA256(passDto.getOldPassword());
		if (user.getPassword().matches(oldPass)) {
			user.setPassword(Cryptography.cryptSHA256(passDto.getNewPassword()));
			userService.saveUser(user);
		} else {
			throw new InvalidUserCredentialsException("Wrong password!");
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<Integer> signUp(@RequestBody SignUpDTO signUp, HttpServletRequest request,
			HttpServletResponse response) throws InvalidUserCredentialsException, ElementNotFoundException,
			NoSuchAlgorithmException, UnauthorizedException, IOException {

		new UserValidation().validate(signUp);
		if (SessionManager.isLogged(request)) {
			throw new UnauthorizedException("Please log out before you can register!");
		}

		if (userService.emailIsTaken(signUp.getEmail())) {
			throw new InvalidUserCredentialsException("That email is already taken!");
		}

		User user = new User();
		new ModelMapper().map(signUp, user);
		user.setPassword(Cryptography.cryptSHA256(signUp.getPassword()));
		user.setGenderId(genderService.findByGenderName(signUp.getGender()).getId());

		SessionManager.signInUser(request, user);
		
		return new ResponseEntity<Integer>(userService.saveUser(user), HttpStatus.OK);

	}

	@PostMapping("/login")
	public ResponseEntity<Integer> login(@RequestBody LoginDTO login, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, ElementNotFoundException,
			UnauthorizedException {
		if (!SessionManager.isLogged(request)) {
			new UserValidation().validate(login);
			User user = userService.findUserByEmail(login.getEmail());

			if (user.getPassword().matches(Cryptography.cryptSHA256(login.getPassword()))) {
//			response.setHeader("location", "https://google.bg");
//			HttpHeaders headers = new HttpHeaders();
				SessionManager.signInUser(request, user);
				return new ResponseEntity<>(user.getId(), HttpStatus.OK);
			} else {
				throw new InvalidUserCredentialsException("Wrong login credentials!");
			}
		} else {
			throw new UnauthorizedException("Log out first before you log in!");
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) throws UnauthorizedException {
		if (SessionManager.isLogged(request)) {
			String message = SessionManager.logOutUser(request);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} else {
			throw new UnauthorizedException("You are not logged in!");
		}
	}

}
