package com.bacefook.utility;

import java.time.LocalDate;
import java.time.Period;

import com.bacefook.dto.LoginDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.InvalidUserCredentialsException;

public class UserValidation {

	private static final int MIN_NAMES_LENGTH = 3;
	private static final int MIN_AGE = 12;
	private static final int MAX_AGE = 120;
	private final static String EMAIL_PATTERN = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,5}$";
	private final static String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8,30}$";
	
	private void validateEmail(String email) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(email) || !email.matches(EMAIL_PATTERN)) {
			throw new InvalidUserCredentialsException("Invalid email format!");
		}
	}

	private void validatePassword(String pass1) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(pass1) || !pass1.matches(PASSWORD_PATTERN)) {
			throw new InvalidUserCredentialsException("Invalid password, must only contain latin letters and numbers");
		}
	}

	private void validateBirthday(LocalDate birthday) throws InvalidUserCredentialsException {
		Period period = Period.between(birthday, LocalDate.now());
		if (period.getYears() >= MAX_AGE && period.getYears() < MIN_AGE) {
			throw new InvalidUserCredentialsException(
					"Invalid birth date, must be older than " + MIN_AGE + 
					" and younger than "+ MAX_AGE + "!");
		}
	}
	
	private void validateName(String name) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(name) || name.length() < MIN_NAMES_LENGTH) {
			throw new InvalidUserCredentialsException("Invalid name, must be longer than " + MIN_NAMES_LENGTH + "!");
		}
	}
	
	private boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public void confirmPassword(String password, String passwordConfirmation) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(password) || !password.equals(passwordConfirmation)) {
			throw new InvalidUserCredentialsException("Credentials do not match!");
		}
	}
	
	public void validate(SignUpDTO signUp) throws InvalidUserCredentialsException {
		validateName(signUp.getFirstName());
		validateName(signUp.getLastName());
		validateEmail(signUp.getEmail());
		validatePassword(signUp.getPassword());
		confirmPassword(signUp.getPassword(), signUp.getPasswordConfirmation());
		validateBirthday(signUp.getBirthday());
	}
	
	public void validate(LoginDTO login) throws InvalidUserCredentialsException {
		if (isNullOrEmpty(login.getEmail())) {
			throw new InvalidUserCredentialsException("Email must not be empty!");
		}
		
		if (isNullOrEmpty(login.getPassword())) {
			throw new InvalidUserCredentialsException("Password must not be empty!");
		}
	}

	

	
}
