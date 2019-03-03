package com.bacefook.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.ChangePasswordDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.GenderNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.exception.UserNotLoggedException;
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

	@PostMapping("/users/{id}/changepassword")
	public void changeUserPassword(@PathVariable("id") int id, @RequestBody ChangePasswordDTO passDto,
			HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException, UserNotLoggedException {
		UserValidation validation = new UserValidation();
		validation.validatePassword(passDto.getNewPassword());
		validation.confirmPassword(passDto.getNewPassword(), passDto.getConfirmPassword());
		try {
			User user = userService.findUserById(id);
			if (SessionManager.isLogged(request)) {
				if (user.getPassword().matches(Cryptography.cryptSHA256(passDto.getOldPassword()))) {
					user.setPassword(Cryptography.cryptSHA256(passDto.getNewPassword()));
				} else {
					throw new InvalidUserCredentialsException("Wrong password!");
				}
			}
			throw new UserNotLoggedException("This user is not logged!");
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/signup")
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

	@PostMapping("/login")
	public int login(@RequestBody LoginDTO login, HttpServletRequest request)
			throws InvalidUserCredentialsException, NoSuchAlgorithmException {
		UserValidation validation = new UserValidation();
		validation.validate(login);
		try {
			User user = userService.findUserByEmail(login.getEmail());
			if (user.getPassword().matches(Cryptography.cryptSHA256(login.getPassword()))) {
				SessionManager.signInUser(request, user);
				return user.getId();
			} else {
				throw new InvalidUserCredentialsException("Credentials do not match!");
			}
		} catch (UserNotFoundException e) {
			throw new InvalidUserCredentialsException("Credentials do not match!");
		}
	}
}
