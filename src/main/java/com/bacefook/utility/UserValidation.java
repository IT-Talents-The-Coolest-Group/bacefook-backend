package com.bacefook.utility;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bacefook.exception.InvalidUserException;

public class UserValidation {
	private static final int MAX_AGE = 120;
	private static final int MIN_AGE = 12;
	private final static Pattern EMAIL_PATTERN = Pattern
			.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
	private final static Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,14}$");

	public static boolean isValidEmail(String email) throws InvalidUserException {
		if (email != null) {
			Matcher regMatcher = EMAIL_PATTERN.matcher(email);
			if (regMatcher.matches()) {
				return true;
			}
		}
		throw new InvalidUserException("Wrong sign up credentials!");
	}

	public static boolean isValidPassword(String pass1, String pass2) throws InvalidUserException {
		if (pass1 != null && pass2 != null) {
			Matcher passMatcher = PASSWORD_PATTERN.matcher(pass1);
			if (passMatcher.matches()) {
				if (pass1.equals(pass2)) {
					return true;
				}
			}
		}
		throw new InvalidUserException("Wrong sign up credentials");
	}

	public static boolean isValidBirthday(LocalDate birthday) throws InvalidUserException {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		// convert String to LocalDate
//		LocalDate birthday = LocalDate.parse(localDate, formatter);
		Period period = Period.between(birthday, LocalDate.now());
		if (period.getYears() >= MAX_AGE && period.getYears() < MIN_AGE) {
			throw new InvalidUserException("Wrong sign up credentials!");
		}
		return true;
	}
}
