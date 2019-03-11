package com.bacefook.exception;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.management.relation.RelationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<Object> handleDateTimeParseError(InvalidFormatException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<Object> handleSQLIntegrityConstraintViolatingError(
			SQLIntegrityConstraintViolationException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ NoSuchAlgorithmException.class })
	public ResponseEntity<Object> handleNoSuchAlgorithmError(NoSuchAlgorithmException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ ElementNotFoundException.class })
	public ResponseEntity<Object> handleUserNotFoundError(ElementNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ UnauthorizedException.class })
	public ResponseEntity<Object> UnauthorizedError(UnauthorizedException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ InvalidUserCredentialsException.class })
	public ResponseEntity<Object> handleInvalidUserError(InvalidUserCredentialsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler({AlreadyContainsException.class})
	public ResponseEntity<Object> handleAlreadyContainsError(AlreadyContainsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	} 
	
	@ExceptionHandler(RelationException.class)
	public ResponseEntity<Object> handleRelationError(RelationException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnprocessableFileException.class)
	public ResponseEntity<Object> handleUnprocessableFileError(UnprocessableFileException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Object> handleNullPointerError(NullPointerException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}