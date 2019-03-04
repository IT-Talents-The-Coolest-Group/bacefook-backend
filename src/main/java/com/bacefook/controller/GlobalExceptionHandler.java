package com.bacefook.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bacefook.exception.GenderNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.exception.UserExistsException;
import com.bacefook.exception.UserNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<String> handleDateTimeParseError(InvalidFormatException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointerError(NullPointerException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<String> handleSQLIntegrityConstraintViolatingError(
			SQLIntegrityConstraintViolationException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ NoSuchAlgorithmException.class })
	public ResponseEntity<String> handleNoSuchAlgorithmError(NoSuchAlgorithmException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ UserNotFoundException.class })
	public ResponseEntity<String> handleUserNotFoundError(UserNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ UserExistsException.class })
	public ResponseEntity<String> handleUserExistsError(UserExistsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler({ UnauthorizedException.class })
	public ResponseEntity<String> UnauthorizedError(UnauthorizedException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ GenderNotFoundException.class })
	public ResponseEntity<String> handleGenderNotFoundError(GenderNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ InvalidUserCredentialsException.class })
	public ResponseEntity<String> handleInvalidUserError(InvalidUserCredentialsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}
	
}
