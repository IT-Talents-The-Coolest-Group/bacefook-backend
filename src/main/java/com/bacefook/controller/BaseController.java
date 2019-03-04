package com.bacefook.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bacefook.exception.GenderNotFoundException;
import com.bacefook.exception.InvalidEmailException;
import com.bacefook.exception.InvalidPasswordException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.PasswordMatchingException;
import com.bacefook.exception.UserExistsException;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.exception.UserNotLoggedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;


@ControllerAdvice
public class BaseController {
	@ExceptionHandler({ InvalidFormatException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleDateTimeParseError(InvalidFormatException e) {
		return "Wrong date format!";
	}

	@ExceptionHandler({ NullPointerException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleNullPointerError() {
		return "Something went wrong!";
	}

	@ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleSQLIntegrityConstraintViolatingError() {
		return "Some DB columns are not filled";
	}

	@ExceptionHandler({ UserNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleUserNotFoundError(UserNotFoundException e) {
		return e.getMessage();
	}

	@ExceptionHandler({ UserExistsException.class })
	@ResponseStatus(HttpStatus.OK)
	public String handleUserExistsError() {
		return "User already exists!";
	}

	@ExceptionHandler({ UserNotLoggedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleUserNotLoggedError(UserNotLoggedException e) {
		return e.getMessage();//TODO parse to JSON
	}

	@ExceptionHandler({ GenderNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleGenderNotFoundError() {
		return "Invalid gender!";
	}

	@ExceptionHandler({ NoSuchAlgorithmException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoSuchAlgorithmError() {
		return "No such algorithm for passwor crypting";
	}

	@ExceptionHandler({ InvalidEmailException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleInvalidEmailError(InvalidEmailException e) {
		return e.getMessage();
	}

	@ExceptionHandler({ InvalidPasswordException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleInvalidPasswordsError(InvalidPasswordException e) {
		return e.getMessage();
	}

	@ExceptionHandler({ PasswordMatchingException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleUnmatchedPasswordsError() {
		return "Confirmation password does not match!";
	}

	@ExceptionHandler({ InvalidUserCredentialsException.class })
	@ResponseStatus
	public String handleInvalidUserError(InvalidUserCredentialsException e) {
		return e.getMessage();
	}
}
