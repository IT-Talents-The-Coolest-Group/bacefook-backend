package com.bacefook.utility;

import java.time.LocalDate;
import java.time.Period;

import com.bacefook.dto.ChangePasswordDTO;
import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.InvalidUserCredentialsException;

public class UserValidation {

	private static final int MIN_NAMES_LENGTH = 3;
	private static final int MAX_NAMES_LENGTH = 30;
	private static final int MIN_AGE = 13;
	private static final int MAX_AGE = 120;
	private final static String EMAIL_PATTERN = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,5}$";
	private final static String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8,30}$";

	private UserValidation() {
		// this is to disable instances
	}
	
	private static void validateEmail(String email) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(email) || !email.matches(EMAIL_PATTERN)) {
			throw new InvalidUserCredentialsException("Invalid email format!");
		}
	}

	public static void validatePassword(String password) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(password) || !password.matches(PASSWORD_PATTERN)) {
			throw new InvalidUserCredentialsException("Invalid password, must only contain latin letters and numbers");
		}
	}

	private static void validateBirthday(LocalDate birthday) throws InvalidUserCredentialsException{
		Period period = Period.between(birthday, LocalDate.now());
		if (period.getYears() >= MAX_AGE || period.getYears() < MIN_AGE) {
			throw new InvalidUserCredentialsException(
					"Invalid birth date, must be older than " + (MIN_AGE - 1) + " and younger than " + MAX_AGE + "!");
		}
	}

	private static void validateName(String name) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(name) || name.length() < MIN_NAMES_LENGTH || name.length() > MAX_NAMES_LENGTH) {
			throw new InvalidUserCredentialsException("Invalid name, must be longer than " + MIN_NAMES_LENGTH + "!");
		}
	}

	private static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static void confirmPassword(String password, String passwordConfirmation) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(password) || !password.equals(passwordConfirmation)) {
			throw new InvalidUserCredentialsException("Passwords do not match!");
		}
	}

	public static void validate(SignUpDTO signUp) throws InvalidUserCredentialsException {
		validateName(signUp.getFirstName());
		validateName(signUp.getLastName());
		validateEmail(signUp.getEmail());
		validatePassword(signUp.getPassword());
		confirmPassword(signUp.getPassword(), signUp.getPasswordConfirmation());
		validateBirthday(signUp.getBirthday());
	}

	public static void validate(LoginDTO login) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(login.getEmail())) {
			throw new InvalidUserCredentialsException("Email must not be empty!");
		}

		if (isNullOrEmpty(login.getPassword())) {
			throw new InvalidUserCredentialsException("Password must not be empty!");
		}
	}
	
	public static void validate(ChangePasswordDTO passChange) throws InvalidUserCredentialsException {
		validatePassword(passChange.getNewPassword());
		confirmPassword(passChange.getNewPassword(), passChange.getConfirmPassword());		
	}

}
